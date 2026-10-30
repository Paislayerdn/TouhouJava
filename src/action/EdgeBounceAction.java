package action;

import entity.Thing;
import main.Debug;

final class EdgeBounceAction extends Action {
	private final Object left;
	private final Object right;
	private final Object bottom;
	private final Object top;
	private final boolean symmetric;

	@Override
	public boolean consumesFrame() { return false; }

	EdgeBounceAction(Object left, Object right, Object bottom, Object top) {
		this.left = left;
		this.right = right;
		this.bottom = bottom;
		this.top = top;
		this.symmetric = false;
	}

	EdgeBounceAction(Object halfWidth, Object halfHeight) {
		this.left = halfWidth;
		this.right = halfWidth;
		this.bottom = halfHeight;
		this.top = halfHeight;
		this.symmetric = true;
	}

	@Override
	public void start() {
		if (!(owner instanceof Thing)) {
			throw Debug.terminate("JScratch", this, "Owner must be a Thing");
		}

		float minX;
		float maxX;
		float minY;
		float maxY;
		if (symmetric) {
			float halfWidth = resolveFloat(left);
			float halfHeight = resolveFloat(bottom);
			minX = -halfWidth;
			maxX = halfWidth;
			minY = -halfHeight;
			maxY = halfHeight;
		} else {
			minX = resolveFloat(left);
			maxX = resolveFloat(right);
			minY = resolveFloat(bottom);
			maxY = resolveFloat(top);
		}

		if (minX > maxX || minY > maxY) {
			throw Debug.terminate("JScratch", this,
				"Bounce bounds must be ordered left-to-right and bottom-to-top");
		}

		float x = owner.getX();
		float y = owner.getY();
		float radians = (float) Math.toRadians(owner.getTrueAngle());
		float dx = (float) Math.cos(radians);
		float dy = (float) Math.sin(radians);

		boolean bounceX = (x <= minX && dx < 0.0f)
			|| (x >= maxX && dx > 0.0f);
		boolean bounceY = (y <= minY && dy < 0.0f)
			|| (y >= maxY && dy > 0.0f);

		if (bounceX) dx = -dx;
		if (bounceY) dy = -dy;

		if (bounceX || bounceY) {
			owner.setTrueAngle((float) Math.toDegrees(Math.atan2(dy, dx)));
		}

		owner.setXY(
			Math.max(minX, Math.min(maxX, x)),
			Math.max(minY, Math.min(maxY, y))
		);
		finish();
	}
}
