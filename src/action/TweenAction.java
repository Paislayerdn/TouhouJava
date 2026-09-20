package action;

public final class TweenAction extends Action {
	private final Object start;
	private final Object end;
	private final int frames;
	private final Easing easing;

	private final ReservedVariable property;
	private float startValue;
	private int elapsed;

	@Override
	public boolean consumesFrame() { return true; }

	public TweenAction(String propertyName, Object end, int frames, Easing easing) {
		this(propertyName, Value.Get(propertyName), end, frames, easing);
	}

	public TweenAction(String propertyName, Object start, Object end, int frames, Easing easing) {
		this.property = ReservedVariable.fromName(propertyName);

		if (property == null) {
			throw new IllegalArgumentException("Unknown Tween property: " + propertyName);
		}

		if (property == ReservedVariable.X || property == ReservedVariable.Y) {
			JDebug.log(
				"[JScratch] Warning: Tween(\"" + property.getName() + "\", ...) "
				+ "is discouraged. Use "
				+ (property == ReservedVariable.X ? "SetX(...)" : "SetY(...)")
				+ " instead."
			);
		}

		this.start = start;
		this.end = end;
		this.frames = frames;
		this.easing = easing;
	}
	
	public ReservedVariable getProperty() { return property; }
	
	@Override
	public void start() {
		if (frames < 0) {
			throw new IllegalArgumentException("[JScratch] Tween frames cannot be negative: " + frames);
		}

		startValue = resolveFloat(start);
		elapsed = 0;

		property.set(this, startValue);

		if (frames == 0) {
			JDebug.log("Warning: Tween with 0 frames. Why the heck are you doing this?");

			property.set(this, resolveFloat(end));
			finish();
		}
	}

	@Override
	public void update() {
		elapsed++;

		float endValue = resolveFloat(end);

		float t = (float) elapsed / frames;
		t = Math.min(t, 1.0f);

		float eased = easing.apply(t);

		float value = startValue + (endValue - startValue) * eased;

		property.set(this, value);

		if (elapsed >= frames) {
			property.set(this, endValue);
			finish();
		}
	}
}