package action;

import entity.Thing;

public final class LookTowardsAction extends Action {
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

		if ( owner.getAngleOverride() ) { owner.setAppearAngle(angle); } else { owner.setTrueAngle(angle); }

		finish();
	}
}
