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
		double dx = target.getX() - owner.getX();
		double dy = target.getY() - owner.getY();

		double angle = Math.toDegrees(Math.atan2(dy, dx));

		if (owner instanceof Entity) {
			Entity entity = (Entity) owner;
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
		double amount = resolveDouble(value);
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

	private void applyActiveAngle(double value) {
		if (owner instanceof Entity) {
			Entity entity = (Entity) owner;

			if (entity.getAngleOverride()) {
				applyAppearAngle(value);
			} else {
				applyTrueAngle(value);
			}
		} else {
			applyTrueAngle(value);
		}
	}
	
	private void applyTrueAngle(double value) {
		switch (operation) {
			case SET:
				owner.setTrueAngle(value); break;
			case CHANGE:
				owner.setTrueAngle(owner.getTrueAngle() + value); break;
		}
	}

	private void applyAppearAngle(double value) {
		if (!(owner instanceof Entity)) {
			JDebug.log(
				"[JScratch] Warning: AppearAngle used on a Thing. "
				+ "Using trueAngle instead."
			);

			applyTrueAngle(value);
			return;
		}
		Entity entity = (Entity) owner;
		switch (operation) {
			case SET: entity.setAppearAngle(value); break;
			case CHANGE: entity.setAppearAngle(entity.getAppearAngle() + value); break;
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
		if (!(owner instanceof Entity)) {
			JDebug.log(
				"[JScratch] Warning: AngleOverride used on a Thing. "
				+ "Ignoring."
			);

			finish();
			return;
		}
		Entity entity = (Entity) owner;
		entity.setAngleOverride(enabled);
		finish();
	}
}