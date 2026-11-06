package action;

import entity.Thing;

public final class ForwardAction extends Action {
	private Object distance;
	@Override
	public boolean consumesFrame() { return false; }

	public ForwardAction(Object distance) {
		this.distance = distance;
	}

	@Override
	public void start() {
		Thing thing = ActionUtil.requireOwner(this, Thing.class);
		float ddDistance = resolveFloat(this.distance);

		float radians = (float) Math.toRadians(thing.getOrientation().getTrueAngle());
		
		thing.getPosition().set(
			thing.getPosition().getX() + (float) Math.cos(radians) * ddDistance,
			thing.getPosition().getY() + (float) Math.sin(radians) * ddDistance
		);
		finish();
	}
}
