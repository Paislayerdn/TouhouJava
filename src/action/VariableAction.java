package action;

public class VariableAction extends Action {
	public enum Operation {
		DECLARE,
		SET,
		CHANGE
	}

	private String name;
	private Operation operation;
	private Object value;
	@Override
	public boolean consumesFrame() { return false; }

	public VariableAction(String name, Operation operation, Object value) {
		this.name = name;
		this.operation = operation;
		this.value = value;
	}

	
	private static void reservedCheck(String name) {
		if (ReservedProperty.isReserved(name)) {
			throw new IllegalArgumentException(
				"[JScratch VariableAction] Cannot set/change reserved value: "
				+ name
			);
		}
	}
	@Override
	public void start() {
		switch (operation) {
			case DECLARE:
				declareVariable(name, resolve(value));
				break;

			case SET:
				reservedCheck(name);
				setVariable(name, resolve(value));
				break;

			case CHANGE:
				reservedCheck(name);
				Object current = getVariable(name);
				double result =
					((Number) current).doubleValue()
					+ resolveDouble(value);

				setVariable(name, result);
				break;
		}

		finish();
	}
}