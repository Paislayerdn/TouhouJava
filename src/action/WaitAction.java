package action;

class WaitAction extends Action {
	private Object duration;
	private int timer;
	@Override
	public boolean consumesFrame() { return !finished; }

	public WaitAction(Object frames) {
		duration = frames;
		timer = 0;
	}

	@Override
	public void start() { timer = 0; }
	public void rewait() { this.start(); }

	@Override
	public void update() {
		timer++;
		int intDuration = (int)resolveDouble(duration);

		if (timer >= intDuration) {
			finish();
		}
	}
}

class WaitUntilAction extends Action {
	private final Object condition;
	@Override
	public boolean consumesFrame() { return !finished; }

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