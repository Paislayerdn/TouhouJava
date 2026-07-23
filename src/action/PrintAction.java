package action;

public class PrintAction extends Action {
	private String message;

	public PrintAction(String message) {
		this.message = message;
	}


	@Override
	public void start() {
		System.out.println(message);
		finish();
	}


	@Override
	public void update() {

	}
}