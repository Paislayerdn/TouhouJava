package action;

public class TestAction extends Action {
	private int timer = 0;

	@Override
	public void start() {
		System.out.println("Action Started");
	}

	@Override
	public void update() {
		timer++;

		System.out.println("Action Frame: " + timer);

		if (timer >= 60) {
			finish();
			System.out.println("Action Finished");
		}
	}
}