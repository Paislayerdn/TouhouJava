package action;

public interface Easing {
	float apply(float t);

	Easing LINEAR = t -> t;

	Easing QUAD_IN = t -> t * t;

	Easing QUAD_OUT = t -> 1.0f - (1.0f - t) * (1.0f - t);

	Easing QUAD_IN_OUT = t -> {
		if (t < 0.5f) { return 2.0f * t * t; }
		return 1.0f - (float) Math.pow(-2.0f * t + 2.0f, 2.0f) / 2.0f;
	};
}