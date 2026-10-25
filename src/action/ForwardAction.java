package action;

public final class ForwardAction extends Action {
	private Object distance;
	@Override
	public boolean consumesFrame() { return false; }

	public ForwardAction(Object distance) {
		this.distance = distance;
	}

	@Override
	public void start() {
		float ddDistance = resolveFloat(this.distance);

		float radians = (float) Math.toRadians(owner.getTrueAngle());
		
		owner.setXY(
			owner.getX() + (float) Math.cos(radians) * ddDistance,
			owner.getY() + (float) Math.sin(radians) * ddDistance
		);
		finish();
	}
}
