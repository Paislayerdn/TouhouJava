package action;

public interface Value {
	Object get(Action action);
	
	public static Value Get(String name) {
		return action -> {
			ReservedVariable property = ReservedVariable.fromName(name);

			if (property != null) {
				return property.get(action);
			}

			return action.getVariable(name);
		};
	}
}