package action;

import main.Debug;

final class WaitAction extends Action {
	private final Object duration;
	private int timer;
	private boolean consumedFrame;

	@Override
	public boolean consumesFrame() { return consumedFrame; }

	public WaitAction(Object frames) {
		this.duration = frames;
		timer = 0;
	}

	@Override
	public void start() {
		consumedFrame = true;
		timer = 0;

		int intDuration = (int) resolveFloat(duration);

		if (intDuration < 0) {
			Debug.warn("JScratch", this,
				"Why would you wait with negative time. "
				+ "Defaulting to 1 frame. "
				+ "Are you trying to predict the future?"
			);
			intDuration = 1;
		}

		if (intDuration == 0) {
			Debug.log("JScratch", this, "Waiting for 0 frames, aborting this wait.");
			consumedFrame = false;
			finish();
		}
	}

	@Override
	public void update() {
		consumedFrame = true;

		timer++;

		if (timer >= (int) resolveFloat(duration)) {
			finish();
		}
	}
}

final class WaitUntilAction extends Action {
	private final Object condition;
	@Override
	public boolean consumesFrame() { return true; }

	public WaitUntilAction(Object condition) {
		this.condition = condition;
	}

	@Override
	public void update() {
		if (LogicValue.toBoolean(resolve(condition), this)) {
			finish();
		}
	}
}