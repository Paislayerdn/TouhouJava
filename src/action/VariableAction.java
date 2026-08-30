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

	public VariableAction(
		String name,
		Operation operation,
		Object value
	) {
		this.name = name;
		this.operation = operation;
		this.value = value;
	}

	@Override
	public void start() {
		switch (operation) {
			case DECLARE:
				declareVariable(name, value);
				break;

			case SET:
				setVariable(name, value);
				break;

			case CHANGE:
				Object current = getVariable(name);

				if (!(current instanceof Number)
						|| !(value instanceof Number)) {

					throw new IllegalArgumentException(
						"[JScratch VariableAction] Cannot change non-numeric variable: " + name
					);
				}

				double result =
					((Number) current).doubleValue()
					+ ((Number) value).doubleValue();

				setVariable(name, result);
				break;
		}

		finish();
	}
}