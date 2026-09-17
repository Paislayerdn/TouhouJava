package action;

import entity.Entity;

class LookTowardsAction extends Action {
	private Entity target;
	@Override
	public boolean consumesFrame() { return false; }

	public LookTowardsAction(Entity target) {
		this.target = target;
	}

	@Override
	public void start() {
		float dx = target.getX() - owner.getX();
		float dy = target.getY() - owner.getY();

		float angle = (float) Math.toDegrees(Math.atan2(dy, dx));

		if (owner instanceof Entity entity && entity.getAngleOverride()) {
			entity.setAppearAngle(angle);
		} else {
			owner.setTrueAngle(angle);
		}

		finish();
	}
}

class AngleAction extends Action {
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
		if (owner instanceof Entity entity && entity.getAngleOverride()) {
			applyAppearAngle(value);
		} else {
			applyTrueAngle(value);
		}
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
		if (!(owner instanceof Entity entity)) {
			JDebug.log(
				"[JScratch] Warning: AppearAngle used on a Thing. "
				+ "Using trueAngle instead."
			);

			applyTrueAngle(value);
			return;
		}

		switch (operation) {
			case SET:
				entity.setAppearAngle(value); break;
			case CHANGE:
				entity.setAppearAngle(entity.getAppearAngle() + value); break;
		}
	}
}

class AngleOverrideAction extends Action {
	private final boolean enabled;
	@Override
	public boolean consumesFrame() { return false; }

	public AngleOverrideAction(boolean enabled) {
		this.enabled = enabled;
	}

	@Override
	public void start() {
		if (!(owner instanceof Entity entity)) {
			JDebug.log(
				"[JScratch] Warning: AngleOverride used on a Thing. "
				+ "Ignoring."
			);

			finish();
			return;
		}

		entity.setAngleOverride(enabled);
		finish();
	}
}