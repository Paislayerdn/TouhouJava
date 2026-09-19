package action;

final class WaitAction extends Action {
	private Object duration;
	private int timer;
	@Override
	public boolean consumesFrame() { return true; }

	public WaitAction() {
		duration = 0;
		timer = 0;
	}
	public WaitAction(Object frames) {
		duration = frames;
		timer = 0;
	}

	@Override
	public void start() { timer = 0; }

	@Override
	public void update() {
		timer++;
		int intDuration = (int) resolveFloat(duration);

		if (timer >= intDuration) {
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