package action;

import main.Debug;

public final class VariableAction extends Action {
	private enum Operation { DECLARE, SET, CHANGE }

	private final String name;
	private final Operation operation;
	private final Object value;
	@Override
	public boolean consumesFrame() { return false; }

	private VariableAction(String name, Operation operation, Object value) {
		this.name = name;
		this.operation = operation;
		this.value = value;
	}

	static VariableAction declare(String name, Object value) {
		return new VariableAction(name, Operation.DECLARE, value);
	}
	static VariableAction set(String name, Object value) {
		return new VariableAction(name, Operation.SET, value);
	}
	static VariableAction change(String name, Object value) {
		return new VariableAction(name, Operation.CHANGE, value);
	}

	@Override
	public void start() {
		boolean targetProperty = getOwner() != null && getOwner().hasScriptProperty(name);

		switch (operation) {
			case DECLARE:
				if (targetProperty) {
					throw Debug.terminate("JScratch", this, "Cannot declare reserved value: "+ name);
				}

				declareVariable(name, resolve(value));
				break;

			case SET:
				if (targetProperty) {
					getOwner().setScriptProperty(name, resolve(value));
				} else {
					setVariable(name, resolve(value));
				}
				break;

			case CHANGE:
				if (targetProperty) {
					float result = ((Number) getOwner().getScriptProperty(name)).floatValue()
						+ resolveFloat(value);
					getOwner().setScriptProperty(name, result);
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
