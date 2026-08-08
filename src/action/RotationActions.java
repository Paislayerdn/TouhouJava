package action;

import entity.Entity;

class LookAction extends Action {
	private Object angle;
	@Override
	public boolean consumesFrame() { return false; }

	public LookAction(Object angle) {
		this.angle = angle;
	}

	@Override
	public void start() {
		double ddAngle = resolveDouble(this.angle);
		owner.setTrueAngle(ddAngle);
		finish();
	}
}

class LookTowardsAction extends Action {
	private Entity target;
	@Override
	public boolean consumesFrame() { return false; }

	public LookTowardsAction(Entity target) {
		this.target = target;
	}

	@Override
	public void start() {
		double dx = target.getX() - owner.getX();
		double dy = target.getY() - owner.getY();

		double angle = Math.toDegrees(Math.atan2(dy, dx));

		owner.setTrueAngle(angle);

		finish();
	}
}

class TurnAction extends Action {
	private Object angle;

	public TurnAction(Object angle) {
		this.angle = angle;
	}

	@Override
	public void start() {
		double ddAngle = resolveDouble(angle);
		
		owner.setTrueAngle(owner.getTrueAngle() + ddAngle);
		finish();
	}
}