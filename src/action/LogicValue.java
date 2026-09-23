package action;

public final class LogicValue {
	private LogicValue() {}

	public static Value And(Object... values) {
		return action -> {
			if (values.length == 0) {
				throw JSCDebug.error(LogicValue.class, "And requires at least 1 value");
			}

			for (Object value : values) {
				if (!toBoolean(action.resolve(value), action)) {
					return false;
				}
			}

			return true;
		};
	}

	public static Value Or(Object... values) {
		return action -> {
			if (values.length == 0) {
				throw JSCDebug.error(LogicValue.class, "Or requires at least 1 value");
			}

			for (Object value : values) {
				if (toBoolean(action.resolve(value), action)) {
					return true;
				}
			}

			return false;
		};
	}

	public static Value Not(Object value) {
		return action -> !toBoolean(action.resolve(value), action);
	}
	protected static boolean toBoolean(Object value, Action action) {
		if (value instanceof Boolean bool) return bool;

		if (value instanceof Number number) {
			float n = number.floatValue();

			if (n == 1) {
				JSCDebug.log(LogicValue.class, "Warning: using Number 1 as true.");
				return true;
			}
			if (n == 0) {
				JSCDebug.log(LogicValue.class, "Warning: using Number 0 as false.");
				return false;
			}
		}

		throw JSCDebug.error(LogicValue.class,
				"Expected Boolean or 0/1, got: " + String.valueOf(value));
	}
}