package action;

import entity.Thing;
import main.Debug;

final class EdgeBounceAction extends Action {
	private final Object left, right;
	private final Object bottom, top;
	@Override
	public boolean consumesFrame() { return false; }

	EdgeBounceAction(Object left, Object right, Object bottom, Object top) {
		this.left = left;
		this.right = right;
		this.bottom = bottom;
		this.top = top;
	}

	EdgeBounceAction(Object halfWidth, Object halfHeight) {
		this.left = (Value) action -> -action.resolveFloat(halfWidth);
		this.right = halfWidth;
		this.bottom = (Value) action -> -action.resolveFloat(halfHeight);
		this.top = halfHeight;
	}

	@Override
	public void start() {
		Thing thing = ActionUtil.requireOwner(this, Thing.class);

		float minX = resolveFloat(left);
		float maxX = resolveFloat(right);
		float minY = resolveFloat(bottom);
		float maxY = resolveFloat(top);

		if (minX > maxX || minY > maxY) throw Debug.terminate("JScratch", this,
				"Bounce bounds must be ordered left-to-right and bottom-to-top");

		float x = thing.getPosition().getX();
		float y = thing.getPosition().getY();
		float radians = (float) Math.toRadians(thing.getOrientation().getTrueAngle());
		float dx = (float) Math.cos(radians);
		float dy = (float) Math.sin(radians);

		boolean bounceX = (x <= minX && dx < 0.0f)
			|| (x >= maxX && dx > 0.0f);
		boolean bounceY = (y <= minY && dy < 0.0f)
			|| (y >= maxY && dy > 0.0f);

		if (bounceX) dx = -dx;
		if (bounceY) dy = -dy;

		if (bounceX || bounceY) thing.getOrientation().setTrueAngle((float) Math.toDegrees(Math.atan2(dy, dx)));

		thing.getPosition().set(
			Math.max(minX, Math.min(maxX, x)),
			Math.max(minY, Math.min(maxY, y))
		);
		finish();
	}
}
