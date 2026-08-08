package action;

public class DestroyAction extends Action {
	@Override
	public boolean consumesFrame() { return false; }

	@Override
	public void start() {
		owner.setAlive(false);
		finish();
	}
}