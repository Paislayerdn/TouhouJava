package action;

public interface Value {
	Object get(Action action);
	
	public static Value Get(String name) {
		return action -> {
			if (action.getOwner() != null && action.getOwner().hasScriptProperty(name)) {
				return action.getOwner().getScriptProperty(name);
			}
			return action.getVariable(name);
		};
	}
}
