package action;

public final class LogicValue {
	private LogicValue() {}

	public static Value And(Object... values) {
		return action -> {
			if (values.length == 0) {
				throw new IllegalArgumentException(
					"[JScratch LogicValue] And requires at least 1 value"
				);
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
				throw new IllegalArgumentException(
					"[JScratch LogicValue] Or requires at least 1 value"
				);
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
		if (value instanceof Boolean bool) {
			return bool;
		}

		if (value instanceof Number number) {
			double n = number.doubleValue();

			if (n == 1) {
				JDebug.log(
					"[JScratch LogicValue] Warning: "
					+ "using Number 1 as true."
				);
				return true;
			}

			if (n == 0) {
				JDebug.log(
					"[JScratch LogicValue] Warning: "
					+ "using Number 0 as false."
				);
				return false;
			}
		}

		throw new IllegalArgumentException(
			"[JScratch LogicValue] Expected Boolean or 0/1, got: "
			+ String.valueOf(value)
		);
	}
}