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
		Thing thing = ActionUtil.requireOwner(this, Thing.class);
		float dx = target.getPosition().getX() - thing.getPosition().getX();
		float dy = target.getPosition().getY() - thing.getPosition().getY();

		float angle = (float) Math.toDegrees(Math.atan2(dy, dx));

		if (thing.getOrientation().getAngleOverride()) thing.getOrientation().setAppearAngle(angle); else {
			thing.getOrientation().setTrueAngle(angle);
		}

		finish();
	}
}
