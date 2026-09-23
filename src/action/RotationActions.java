package action;

import entity.Thing;

final class LookTowardsAction extends Action {
	private final Thing target;
	@Override
	public boolean consumesFrame() { return false; }

	public LookTowardsAction(Thing target) {
		this.target = target;
	}

	@Override
	public void start() {
		float dx = target.getX() - owner.getX();
		float dy = target.getY() - owner.getY();

		float angle = (float) Math.toDegrees(Math.atan2(dy, dx));

		if ( owner.getAngleOverride() ) { owner.setAppearAngle(angle); }
		else { owner.setTrueAngle(angle); }

		finish();
	}
}

final class AngleAction extends Action {
	enum Angle {ACTIVE, TRUE, APPEAR}
	enum Operation {SET, CHANGE}

	private final Angle angle;
	private final Operation operation;
	private final Object value;
	@Override
	public boolean consumesFrame() { return false; }

	public AngleAction(Angle angle, Operation operation, Object value) {
		this.angle = angle;
		this.operation = operation;
		this.value = value;
	}

	@Override
	public void start() {
		float amount = resolveFloat(value);
		switch (angle) {
			case ACTIVE:
				applyActiveAngle(amount); break;
			case TRUE:
				applyTrueAngle(amount); break;
			case APPEAR:
				applyAppearAngle(amount); break;
		}

		finish();
	}

	private void applyActiveAngle(float value) {
		if ( owner.getAngleOverride() ) { applyAppearAngle(value); }
		else { applyTrueAngle(value); }
	}
	
	private void applyTrueAngle(float value) {
		switch (operation) {
			case SET:
				owner.setTrueAngle(value); break;
			case CHANGE:
				owner.setTrueAngle(owner.getTrueAngle() + value); break;
		}
	}

	private void applyAppearAngle(float value) {
		switch (operation) {
			case SET:
				owner.setAppearAngle(value); break;
			case CHANGE:
				owner.setAppearAngle(owner.getAppearAngle() + value); break;
		}
	}
}

final class AngleOverrideAction extends Action {
	private final boolean enabled;
	@Override
	public boolean consumesFrame() { return false; }

	public AngleOverrideAction(boolean enabled) {
		this.enabled = enabled;
	}

	@Override
	public void start() {
		owner.setAngleOverride(enabled);
		finish();
	}
}