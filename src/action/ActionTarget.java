package action;

import entity.Appearance;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

public abstract class ActionTarget {
	private final Map<String, FieldBinding> properties = new HashMap<>();
	private ActionRunner actions = new ActionRunner();
	private String name = "[UNNAMED TARGET]";
	private boolean alive = true;
	private Appearance appearance;

	protected final void registerScriptComponents(Object... components) {
		for (Object component : components) {
			if (component == null) continue;
			if (component instanceof Appearance value) appearance = value;

			for (Class<?> type = component.getClass(); type != null; type = type.getSuperclass()) {
				for (Field field : type.getDeclaredFields()) {
					ScriptProperty annotation = field.getAnnotation(ScriptProperty.class);
					if (annotation == null || Modifier.isStatic(field.getModifiers())) continue;
					String propertyName = annotation.value().isEmpty()
						? field.getName() : annotation.value();
					if (properties.putIfAbsent(propertyName,
						new FieldBinding(component, field)) != null) {
						throw new IllegalStateException("Duplicate script property: " + propertyName);
					}
					field.setAccessible(true);
				}
			}
		}
	}

	public final boolean hasScriptProperty(String name) {
		return properties.containsKey(name)
			|| name.equals("angle") && this instanceof entity.Thing;
	}

	/** Returns the registered spelling for a script property, or null if it is absent. */
	public final String findScriptProperty(String name, boolean ignoreCase) {
		for (String property : properties.keySet()) {
			if (ignoreCase ? property.equalsIgnoreCase(name) : property.equals(name)) return property;
		}
		if (this instanceof entity.Thing && (ignoreCase
				? "angle".equalsIgnoreCase(name) : "angle".equals(name))) return "angle";
		return null;
	}

	public final Object getScriptProperty(String name) {
		if (name.equals("angle") && this instanceof entity.Thing thing) {
			return thing.getOrientation().getAngle();
		}
		FieldBinding binding = properties.get(name);
		if (binding == null) throw new IllegalArgumentException(
			"Script property '" + name + "' is not available on " + getName());
		try {
			return binding.field.get(binding.component);
		} catch (IllegalAccessException exception) {
			throw new IllegalStateException("Unable to read script property " + name, exception);
		}
	}

	public final void setScriptProperty(String name, Object value) {
		if (name.equals("angle") && this instanceof entity.Thing thing) {
			thing.getOrientation().setAngle(((Number) value).floatValue());
			return;
		}
		FieldBinding binding = properties.get(name);
		if (binding == null) throw new IllegalArgumentException(
			"Script property '" + name + "' is not available on " + getName());
		try {
			if (binding.component instanceof ScriptPropertySetter setter
					&& setter.setScriptProperty(name, value)) return;
			binding.field.set(binding.component, convert(value, binding.field.getType()));
		} catch (IllegalAccessException exception) {
			throw new IllegalStateException("Unable to write script property " + name, exception);
		}
	}

	private static Object convert(Object value, Class<?> type) {
		if (type == float.class || type == Float.class) return ((Number) value).floatValue();
		if (type == double.class || type == Double.class) return ((Number) value).doubleValue();
		if (type == int.class || type == Integer.class) return ((Number) value).intValue();
		if (type == long.class || type == Long.class) return ((Number) value).longValue();
		if (type == boolean.class || type == Boolean.class) return value;
		if (type.isInstance(value)) return value;
		throw new IllegalArgumentException("Cannot assign " + value + " to " + type.getSimpleName());
	}

	public final ActionRunner getActionRunner() { return actions; }
	public final ActionContext getActionContext() { return actions.getContext(); }
	public final void setActionContext(ActionContext context) { actions = new ActionRunner(context); }
	public final Object getVariable(String name) { return actions.getContext().get(name); }
	public final void updateActions() { actions.update(); }
	public final void run(Action action) { actions.add(action, this); }
	public final float getX() { return ((Number) getScriptProperty("x")).floatValue(); }
	public final float getY() { return ((Number) getScriptProperty("y")).floatValue(); }
	public final void setX(float value) { setScriptProperty("x", value); }
	public final void setY(float value) { setScriptProperty("y", value); }
	public final void setXY(float x, float y) { setX(x); setY(y); }
	public final void move(float dx, float dy) { setXY(getX() + dx, getY() + dy); }
	public final float getTrueAngle() { return ((Number) getScriptProperty("trueAngle")).floatValue(); }
	public final void setTrueAngle(float value) { setScriptProperty("trueAngle", value); }
	public final float getAppearAngle() { return ((Number) getScriptProperty("appearAngle")).floatValue(); }
	public final void setAppearAngle(float value) { setScriptProperty("appearAngle", value); }
	public final boolean getAngleOverride() { return (Boolean) getScriptProperty("angleOverride"); }
	public final void setAngleOverride(boolean value) { setScriptProperty("angleOverride", value); }
	public final float getAngle() { return ((Number) getScriptProperty("angle")).floatValue(); }
	public final void setAngle(float value) { setScriptProperty("angle", value); }

	public Appearance getAppearance() {
		if (appearance == null) throw new IllegalStateException(
			getName() + " has no appearance component");
		return appearance;
	}

	public final boolean isAlive() { return alive; }
	public final void setAlive(boolean value) { alive = value; }
	public final void destroy() { alive = false; }
	public final String getName() { return name; }
	public final void setName(String value) { name = value; }

	public ActionTarget spawn(String className, String instanceName) {
		return SpawnRegistry.create(className, instanceName);
	}

	public void onBestowed() {}

	private record FieldBinding(Object component, Field field) {}
}
