package action;

public class MoveAction extends Action {

	private double speed;


	public MoveAction(double speed) {
		this.speed = speed;
	}


	@Override
	public void update() {

		owner.move(speed, 0);

	}
}