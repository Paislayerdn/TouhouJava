package action;

public final class VariableAction extends Action {
	public enum Operation { DECLARE, SET, CHANGE }

	private final String name;
	private final Operation operation;
	private final Object value;
	@Override
	public boolean consumesFrame() { return false; }

	public VariableAction(String name, Operation operation, Object value) {
		this.name = name;
		this.operation = operation;
		this.value = value;
	}

	@Override
	public void start() {
		ReservedVariable property = ReservedVariable.fromName(name);

		switch (operation) {
			case DECLARE:
				if (property != null) {
					throw new IllegalArgumentException("[JScratch VariableAction] Cannot declare reserved value: "+ name);
				}

				declareVariable(name, resolve(value));
				break;

			case SET:
				if (property != null) {
					property.warnUsage("Set");
					property.set(this, resolveFloat(value));
				} else {
					setVariable(name, resolve(value));
				}
				break;

			case CHANGE:
				if (property != null) {
					property.warnUsage("Change");

					float result =
						((Number) property.get(this)).floatValue()
						+ resolveFloat(value);

					property.set(this, result);
				} else {
					Object current = getVariable(name);

					float result =
						((Number) current).floatValue()
						+ resolveFloat(value);

					setVariable(name, result);
				}
				break;
		}

		finish();
	}
}