package action;

public class WaitAction extends Action {
	private Object duration;
	private int timer;

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