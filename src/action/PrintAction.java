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
		JDebug.log(this, String.format(
			"msg=%s context=%d ",
			resolve(message),
			System.identityHashCode(getContext())
		));
		finish();
	}
}