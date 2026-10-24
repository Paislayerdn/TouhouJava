package action;

import main.Debug;

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

		this.start = start;
		this.end = end;
		this.frames = frames;
		this.easing = easing;
		this.mode = mode;
	}
	
	public String getPropertyName() { return propertyName; }
	private float getValue() {
		if (getOwner() != null && getOwner().hasScriptProperty(propertyName)) {
			return resolveFloat(getOwner().getScriptProperty(propertyName));
		}

		return resolveFloat(getVariable(propertyName));
	}

	private void setValue(float value) {
		if (getOwner() != null && getOwner().hasScriptProperty(propertyName)) {
			getOwner().setScriptProperty(propertyName, value);
		} else {
			setVariable(propertyName, value);
		}
	}
	
	@Override
	public void start() {
		resolvedFrames = (int) resolveFloat(frames);
		if (resolvedFrames < 0) {
			throw Debug.terminate("JScratch", this, "Tween frames cannot be negative: " + resolvedFrames);
		}

		startValue = resolveFloat(start);

		if (mode == TweenMode.SNAPSHOT) {
			endValue = resolveFloat(end);
		}

		elapsed = 0;

		setValue(startValue);

		if (resolvedFrames == 0) {
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
