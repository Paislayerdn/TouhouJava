package action;

public interface Value {
	Object get(Action action);
	
	public static Value Get(String name) {
		return action -> action.getVariable(name);
	}
}