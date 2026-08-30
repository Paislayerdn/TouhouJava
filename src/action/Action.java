package action;

import entity.Entity;

public abstract class Action {
	protected boolean finished = false;
	protected Entity owner;
	protected ActionContext context;
	public boolean consumesFrame() { return true;}

	public boolean isFinished() { return finished; }
	public void finish() { finished = true; }
	public void reset() { finished = false; }
	
	public Entity getOwner() { return owner; }
	public void setOwner(Entity owner) { this.owner = owner; }
	
	public ActionContext getContext() { return context; }
	public void setContext(ActionContext context) { this.context = context; }

	public void declareVariable(String name, Object value) { context.declare(name, value); }
	public Object getVariable(String name) { return context.get(name); }
	public void setVariable(String name, Object value) { context.set(name, value); }

	
	public Object rs(Object value) { return resolve(value); }
	public Object resolve(Object value) {
		if (value instanceof Value) {
			return ((Value) value).get(this);
		}

		return value;
	}

	public double rsD(Object value) { return resolveDouble(value); }
	public double resolveDouble(Object value) {
		return ((Number) resolve(value)).doubleValue();
	}
	
	public void start() {}
	public void update() {}
}

final class JDebug {
	private JDebug() {}

	public static void log(Class<?> source, String message) {
		System.out.println(
			"[JScratch " + source.getSimpleName() + "] " + message
		);
	}
	public static void log(Object source, String message) {
		System.out.println(
			"[JScratch " + source.getClass().getSimpleName() + "] " + message
		);
	}
	public static void log(String message) {
		System.out.println("[JScratch] " + message);
	}
}