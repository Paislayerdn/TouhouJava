package action;

enum TweenMode {
	CHASING,
	SNAPSHOT
}

public final class TweenAction extends Action {
	private final Object start;
	private final Object end;
	private final Object frames;
	private final Easing easing;
	private final TweenMode mode;

	private final String propertyName;
	private final ReservedVariable property;
	private float startValue;
	private float endValue;
	private int resolvedFrames;
	private int elapsed;

	@Override
	public boolean consumesFrame() { return true; }

	public TweenAction(String propertyName, Object end, Object frames, Easing easing) {
		this(propertyName, Value.Get(propertyName), end, frames, easing, TweenMode.CHASING);
	}
	public TweenAction(String propertyName, Object end, Object frames, Easing easing, TweenMode mode) {
		this(propertyName, Value.Get(propertyName), end, frames, easing, mode);
	}
	public TweenAction(String propertyName, Object start, Object end, Object frames, Easing easing) {
		this(propertyName, start, end, frames, easing, TweenMode.CHASING);
	}
	public TweenAction(String propertyName, Object start, Object end, Object frames, Easing easing, TweenMode mode) {
		this.propertyName = propertyName;
		this.property = ReservedVariable.fromName(propertyName);

		if (property != null) {
			if (property == ReservedVariable.X || property == ReservedVariable.Y) {
				JSCDebug.log(
					"Warning: Tween(\"" + property.getName() + "\", ...) is discouraged. Use "
					+ (property == ReservedVariable.X ? "SetX(...)" : "SetY(...)")
					+ " instead."
				);
			}
		}

		this.start = start;
		this.end = end;
		this.frames = frames;
		this.easing = easing;
		this.mode = mode;
	}
	
	public String getPropertyName() { return propertyName; }
	public ReservedVariable getProperty() { return property; }
	private float getValue() {
		if (property != null) {
			return resolveFloat(property.get(this));
		}

		return resolveFloat(getVariable(propertyName));
	}

	private void setValue(float value) {
		if (property != null) {
			property.set(this, value);
		} else {
			setVariable(propertyName, value);
		}
	}
	
	@Override
	public void start() {
		resolvedFrames = (int) resolveFloat(frames);
		if (resolvedFrames < 0) {
			throw JSCDebug.error(this, "Tween frames cannot be negative: " + frames);
		}

		startValue = resolveFloat(start);

		if (mode == TweenMode.SNAPSHOT) {
			endValue = resolveFloat(end);
		}

		elapsed = 0;

		setValue(startValue);

		if (resolvedFrames == 0) {
			JSCDebug.log("Warning: Tween with 0 frames. Why the heck are you doing this?");

			setValue(resolveFloat(end));
			finish();
		}
	}

	@Override
	public void update() {
		elapsed++;

		float resolvedEnd = mode == TweenMode.SNAPSHOT? endValue: resolveFloat(end);

		float t = (float) elapsed / resolvedFrames;
		t = Math.min(t, 1.0f);

		float eased = easing.apply(t);

		float value = startValue + (resolvedEnd - startValue) * eased;

		setValue(value);

		if (elapsed >= resolvedFrames) {
			setValue(resolvedEnd);
			finish();
		}
	}
}