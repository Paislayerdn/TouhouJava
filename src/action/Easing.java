package action;

public interface Easing {
	float apply(float t);

	Easing Linear = t -> t;

	Easing QuadIn = t -> t * t;
	Easing QuadOut = t -> 1.0f - (1.0f - t) * (1.0f - t);
	Easing QuadInOut = t -> t < 0.5f
		? 2.0f * t * t
		: 1.0f - (float) Math.pow(-2.0f * t + 2.0f, 2.0f) / 2.0f;

	Easing QuintIn = t -> t * t * t * t * t;
	Easing QuintOut = t -> 1.0f - (float) Math.pow(1.0f - t, 5.0f);
	Easing QuintInOut = t -> t < 0.5f
		? 16.0f * t * t * t * t * t
		: 1.0f - (float) Math.pow(-2.0f * t + 2.0f, 5.0f) / 2.0f;

	Easing CircularIn = t -> 1.0f - (float) Math.sqrt(1.0f - t * t);
	Easing CircularOut = t -> (float) Math.sqrt(1.0f - (t - 1.0f) * (t - 1.0f));
	Easing CircularInOut = t -> t < 0.5f
		? (1.0f - (float) Math.sqrt(1.0f - 4.0f * t * t)) / 2.0f
		: ((float) Math.sqrt(1.0f - (-2.0f * t + 2.0f) * (-2.0f * t + 2.0f)) + 1.0f) / 2.0f;

	Easing BackIn = t -> {
		float c3 = 2.70158f;
		return c3 * t * t * t - 1.70158f * t * t;
	};
	Easing BackOut = t -> {
		float shifted = t - 1.0f;
		return 1.0f + 2.70158f * shifted * shifted * shifted
			+ 1.70158f * shifted * shifted;
	};
	Easing BackInOut = t -> {
		float c2 = 1.70158f * 1.525f;
		return t < 0.5f
			? (float) (Math.pow(2.0f * t, 2.0f) * ((c2 + 1.0f) * 2.0f * t - c2)) / 2.0f
			: (float) (Math.pow(2.0f * t - 2.0f, 2.0f) * ((c2 + 1.0f) * (t * 2.0f - 2.0f) + c2) + 2.0f) / 2.0f;
	};

	Easing BounceOut = t -> {
		float n1 = 7.5625f;
		float d1 = 2.75f;
		if (t < 1.0f / d1) return n1 * t * t;
		if (t < 2.0f / d1) {
			t -= 1.5f / d1;
			return n1 * t * t + 0.75f;
		}
		if (t < 2.5f / d1) {
			t -= 2.25f / d1;
			return n1 * t * t + 0.9375f;
		}
		t -= 2.625f / d1;
		return n1 * t * t + 0.984375f;
	};
	Easing BounceIn = t -> 1.0f - BounceOut.apply(1.0f - t);
	Easing BounceInOut = t -> t < 0.5f
		? (1.0f - BounceOut.apply(1.0f - 2.0f * t)) / 2.0f
		: (1.0f + BounceOut.apply(2.0f * t - 1.0f)) / 2.0f;

	Easing ElasticIn = t -> {
		if (t == 0.0f || t == 1.0f) return t;
		float c4 = (float) (2.0 * Math.PI / 3.0);
		return -(float) Math.pow(2.0f, 10.0f * t - 10.0f)
			* (float) Math.sin((10.0f * t - 10.75f) * c4);
	};
	Easing ElasticOut = t -> {
		if (t == 0.0f || t == 1.0f) return t;
		float c4 = (float) (2.0 * Math.PI / 3.0);
		return (float) Math.pow(2.0f, -10.0f * t)
			* (float) Math.sin((10.0f * t - 0.75f) * c4) + 1.0f;
	};
	Easing ElasticInOut = t -> {
		if (t == 0.0f || t == 1.0f) return t;
		float c5 = (float) (2.0 * Math.PI / 4.5);
		return t < 0.5f
			? -((float) Math.pow(2.0f, 20.0f * t - 10.0f)
				* (float) Math.sin((20.0f * t - 11.125f) * c5)) / 2.0f
			: ((float) Math.pow(2.0f, -20.0f * t + 10.0f)
				* (float) Math.sin((20.0f * t - 11.125f) * c5)) / 2.0f + 1.0f;
	};
}
