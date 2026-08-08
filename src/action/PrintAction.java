package action;

public class PrintAction extends Action {
	private final Object message;
	@Override
	public boolean consumesFrame() { return false; }

	public PrintAction(Object message) {
		this.message = message;
	}

	@Override
	public void start() {
		System.out.println("[Print] context=" + System.identityHashCode(getContext())+ " i=" + resolve(message));
		finish();
	}
}