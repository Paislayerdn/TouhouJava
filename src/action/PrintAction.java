package action;

import main.Debug;

public final class PrintAction extends Action {
	private final Object message;
	private final boolean javaNative;
	@Override
	public boolean consumesFrame() { return false; }

	public PrintAction(Object message, boolean java) {
		this.message = message;
		this.javaNative = java;
	}

	@Override
	public void start() {
		String output = String.format("msg=%s context=%d ", resolve(message), System.identityHashCode(getContext()) );
		String namespace = (javaNative)? "JScratch": "JSL";
		Debug.log(namespace, this, output);
		finish();
	}
}