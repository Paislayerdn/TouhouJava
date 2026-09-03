package action;

public class MathValue {
	private MathValue() {}

	public static Value Add(Object... values) {
		return action -> {
			double result = 0;

			for (Object value : values) {
				result += ((Number) action.resolve(value)).doubleValue();
			}

			return result;
		};
	}

	public static Value Sub(Object... values) {
		return action -> {
			if (values.length == 0) return 0.0;

			double result = ((Number) action.resolve(values[0])).doubleValue();

			for (int i = 1; i < values.length; i++) {
				result -= ((Number) action.resolve(values[i])).doubleValue();
			}

			return result;
		};
	}

	public static Value Mul(Object... values) {
		return action -> {
			double result = 1;

			for (Object value : values) {
				result *= ((Number) action.resolve(value)).doubleValue();
			}

			return result;
		};
	}

	public static Value Div(Object... values) {
		return action -> {
			if (values.length == 0) return 1.0;

			double result = ((Number) action.resolve(values[0])).doubleValue();

			for (int i = 1; i < values.length; i++) {
				result /= ((Number) action.resolve(values[i])).doubleValue();
			}

			return result;
		};
	}

	public static Value Min(Object... values) {
		return action -> {
			if (values.length == 0) return 0.0;

			double result = Double.POSITIVE_INFINITY;

			for (Object value : values) {
				result = Math.min(
					result,
					((Number) action.resolve(value)).doubleValue()
				);
			}

			return result;
		};
	}

	public static Value Max(Object... values) {
		return action -> {
			if (values.length == 0) return 0.0;

			double result = Double.NEGATIVE_INFINITY;

			for (Object value : values) {
				result = Math.max(
					result,
					((Number) action.resolve(value)).doubleValue()
				);
			}

			return result;
		};
	}
	
	public static Value Mod(Object... values) {
		return action -> {
			if (values.length == 0) return 0.0;

			double result = ((Number) action.resolve(values[0])).doubleValue();

			for (int i = 1; i < values.length; i++) {
				double divisor = ((Number) action.resolve(values[i])).doubleValue();

				result = ((result % divisor) + divisor) % divisor;
			}

			return result;
		};
	}

	public static Value Power(Object base, Object exponent) {
		return action -> Math.pow(
			action.resolveDouble(base),
			action.resolveDouble(exponent)
		);
	}

	public static Value Root(Object value, Object n) {
		return action -> {
			double degree = action.resolveDouble(n);

			if (degree == 0) {
				throw new IllegalArgumentException("[JScratch MathValue] Root degree cannot be zero");
			}

			return Power(value, 1.0 / degree);
		};
	}
	
	public static Value Random() {
		return action -> Math.random() * Math.nextUp(1.0);
	}

	public static Value Random(Object a, Object b) {
		return action -> {
			int min = (int)action.resolveDouble(a);
			int max = (int)action.resolveDouble(b);

			return min + (int)(Math.random()*(max - min+1));
		};
	}
}