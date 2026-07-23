package action;

public class Wait extends Action {
	private int duration;
	private int timer;

	public Wait(int frames) {
		duration = frames;
		timer = 0;
	}

	@Override
	public void start() {
		timer = 0;
	}

	@Override
	public void update() {
		timer++;
		System.out.println("Waiting Frame: " + timer);

		if (timer >= duration) {
			finish();
			System.out.println("Finished waiting");
		}
	}
}