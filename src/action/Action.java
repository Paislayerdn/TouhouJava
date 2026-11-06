package action;

import main.Debug;


public abstract class Action {
	protected boolean finished = false;
	protected ActionTarget owner;
	protected ActionContext context;
	public abstract boolean consumesFrame();
	
	public void reset() { finished = false; }
	public void start() {}
	public void update() {}
	
	public final boolean isFinished() { return finished; }
	public final void finish() { finished = true; }
	
	public final ActionTarget getOwner() { return owner; }
	public final void setOwner(ActionTarget owner) { this.owner = owner; }
	
	public final ActionContext getContext() { return context; }
	public final void setContext(ActionContext context) { this.context = context; }

	public final void declareVariable(String name, Object value) { context.declare(name, value); }
	public final Object getVariable(String name) { return context.get(name); }
	public final void setVariable(String name, Object value) { context.set(name, value); }

	public final Object resolve(Object value) {
		while (value instanceof Value) {
			value = ((Value) value).get(this);
		}

		return value;
	}
	public final float resolveFloat(Object value) {
		return ((Number) resolve(value)).floatValue();
	}
}

final class ActionUtil {
	private ActionUtil() {}

	public static <T extends ActionTarget> T requireOwner(Action action, Class<T> type) {
		ActionTarget owner = action.getOwner();
		if (owner != null && type.isInstance(owner)) return type.cast(owner);
		String target = owner == null
			? "<no target (null)>"
			: "'" + String.valueOf(owner.getName()) + "' (" + owner.getClass().getName() + ")";
		throw Debug.terminate("JScratch", action,
			"Action " + action.getClass().getSimpleName() + " requires "
				+ type.getSimpleName() + " but target " + target + " was provided");
	}
}
