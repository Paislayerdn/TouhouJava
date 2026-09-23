package action;

final class WaitAction extends Action {
	private final Object duration;
	private int timer;

	@Override
	public boolean consumesFrame() { return true; }

	public WaitAction() {
		duration = 1;
		timer = 0;
	}

	public WaitAction(Object frames) {
		this.duration = frames;
		timer = 0;
	}

	@Override
	public void start() {
		timer = 0;

		int intDuration = (int) resolveFloat(duration);

		if (intDuration < 0) {
			JSCDebug.log(this,
				"Why would you wait with negative time. "
				+ "Defaulting to 1 frame. "
				+ "Are you trying to predict the future?"
			);
			intDuration = 1;
		}

		if (intDuration == 0) {
			JSCDebug.log(this,
				"Why would you wait with 0 frames? "
				+ "Aborting this wait, good luck."
			);
			finish();
		}
	}

	@Override
	public void update() {
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