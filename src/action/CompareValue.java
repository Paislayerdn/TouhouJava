package action;

public final class CompareValue {
	private CompareValue() {}

	public static Value Greater(Object... values) {
		return action -> compare(values, action, Comparison.GREATER);
	}

	public static Value GreaterEqual(Object... values) {
		return action -> compare(values, action, Comparison.GREATER_EQUAL);
	}

	public static Value Less(Object... values) {
		return action -> compare(values, action, Comparison.LESS);
	}

	public static Value LessEqual(Object... values) {
		return action -> compare(values, action, Comparison.LESS_EQUAL);
	}

	public static Value Equal(Object... values) {
		return action -> compare(values, action, Comparison.EQUAL);
	}

	private enum Comparison {
		GREATER,
		GREATER_EQUAL,
		LESS,
		LESS_EQUAL,
		EQUAL
	}

	private static boolean compare(
		Object[] values,
		Action action,
		Comparison comparison
	) {
		if (values.length < 2) {
			throw new IllegalArgumentException(
				"[JScratch CompareValue] Comparison requires at least 2 values"
			);
		}

		Object[] resolved = new Object[values.length];

		boolean hasString = false;
		boolean allNumbers = true;

		for (int i = 0; i < values.length; i++) {
			resolved[i] = action.resolve(values[i]);

			if (resolved[i] instanceof String) {
				hasString = true;
			}

			if (!(resolved[i] instanceof Number)) {
				allNumbers = false;
			}
		}

		if (hasString) {
			JDebug.log(
				"[JScratch CompareValue] Warning: "
				+ "comparing values involving String; "
				+ "using lexicographical String comparison."
			);

			String[] strings = new String[resolved.length];

			for (int i = 0; i < resolved.length; i++) {
				if (!(resolved[i] instanceof String)
					&& !(resolved[i] instanceof Number)) {

					throw new IllegalArgumentException(
						"[JScratch CompareValue] Cannot compare "
						+ resolved[i].getClass().getSimpleName()
						+ " with String"
					);
				}

				strings[i] = String.valueOf(resolved[i]);
			}

			for (int i = 0; i < strings.length - 1; i++) {
				if (!compareStrings(strings[i], strings[i + 1], comparison)) {
					return false;
				}
			}

			return true;
		}

		if (allNumbers) {
			for (int i = 0; i < resolved.length - 1; i++) {
				double a = ((Number) resolved[i]).doubleValue();
				double b = ((Number) resolved[i + 1]).doubleValue();

				if (!compareNumbers(a, b, comparison)) {
					return false;
				}
			}

			return true;
		}

		throw new IllegalArgumentException(
			"[JScratch CompareValue] Cannot compare values of "
			+ "different variable types"
		);
	}

	private static boolean compareNumbers(
		double a,
		double b,
		Comparison comparison
	) {
		return switch (comparison) {
			case GREATER -> a > b;
			case GREATER_EQUAL -> a >= b;
			case LESS -> a < b;
			case LESS_EQUAL -> a <= b;
			case EQUAL -> a == b;
		};
	}

	private static boolean compareStrings(
		String a,
		String b,
		Comparison comparison
	) {
		int result = a.compareTo(b);

		return switch (comparison) {
			case GREATER -> result > 0;
			case GREATER_EQUAL -> result >= 0;
			case LESS -> result < 0;
			case LESS_EQUAL -> result <= 0;
			case EQUAL -> result == 0;
		};
	}
}