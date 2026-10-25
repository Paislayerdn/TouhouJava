package action;

import main.Debug;

public final class TweenAction extends Action {
	private final Object start;
	private final Object end;
	private final Object frames;
	private final Easing easing;

	private final String propertyName;
	private float startValue;
	private float endValue;
	private int resolvedFrames;
	private int elapsed;

	@Override
	public boolean consumesFrame() { return true; }

	public TweenAction(String propertyName, Object end, Object frames, Easing easing) {
		this(propertyName, Value.Get(propertyName), end, frames, easing);
	}
	public TweenAction(String propertyName, Object start, Object end, Object frames, Easing easing) {
		this.propertyName = propertyName;

		this.start = start;
		this.end = end;
		this.frames = frames;
		this.easing = easing;
	}
	
	public String getPropertyName() { return propertyName; }
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
		endValue = resolveFloat(end);

		elapsed = 0;

		setValue(startValue);

		if (resolvedFrames == 0) {
			setValue(endValue);
			finish();
		}
	}

	@Override
	public void update() {
		elapsed++;

		float t = (float) elapsed / resolvedFrames;
		t = Math.min(t, 1.0f);

		float eased = easing.apply(t);

		float value = startValue + (endValue - startValue) * eased;

		setValue(value);

		if (elapsed >= resolvedFrames) {
			setValue(endValue);
			finish();
		}
	}
}
