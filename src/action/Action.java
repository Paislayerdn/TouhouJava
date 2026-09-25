package action;

import main.Debug;

import entity.Thing;
import entity.Entity;

public abstract class Action {
	protected boolean finished = false;
	protected Thing owner;
	protected ActionContext context;

	public abstract boolean consumesFrame();
	public void reset() { finished = false; }
	public void start() {}
	public void update() {}
	
	public final boolean isFinished() { return finished; }
	public final void finish() { finished = true; }
	
	public final Thing getOwner() { return owner; }
	public final void setOwner(Thing owner) { this.owner = owner; }
	
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

	public static Entity requireEntity(Action action) {
		if (action.getOwner() instanceof Entity entity) {
			return entity;
		}
		throw Debug.terminate("JScratch", action, "Owner must be an Entity");
	}
}