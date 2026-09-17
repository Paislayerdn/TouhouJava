package collision;

public final class CollisionChecker {
	private CollisionChecker() {}

	public static CollisionResult check(Hitbox a, Hitbox b) {
		if (!a.isEnabled() || !b.isEnabled()) return null;

		CollisionType type = CollisionSemantics.getType(a, b);

		if (type == null) return null;
		if (!checkHitboxes(a, b)) return null;

		return new CollisionResult(a, b, type);
	}
	
	private static boolean checkHitboxes(Hitbox a, Hitbox b) {
		if (a instanceof CircleHitbox ca &&
			b instanceof CircleHitbox cb) {
			return circleCircle(ca, cb);
		}

		if (a instanceof CircleHitbox ca &&
			b instanceof RectangleHitbox rb) {
			return circleRectangle(ca, rb);
		}

		if (a instanceof RectangleHitbox ra &&
			b instanceof CircleHitbox cb) {
			return circleRectangle(cb, ra);
		}

		if (a instanceof RectangleHitbox ra &&
			b instanceof RectangleHitbox rb) {
			return rectangleRectangle(ra, rb);
		}

		return false;
	}
	
	private static boolean circleCircle(CircleHitbox a, CircleHitbox b) {
		float dx = a.getWorldX() - b.getWorldX();
		float dy = a.getWorldY() - b.getWorldY();

		float distanceSquared = dx * dx + dy * dy;
		float radiusSum = a.getRadius() + b.getRadius();

		return distanceSquared < radiusSum * radiusSum;
	}
	
	private static boolean rectangleRectangle(RectangleHitbox a, RectangleHitbox b) {
		float aLeft = a.getWorldX() - a.getWidth() / 2;
		float aRight = a.getWorldX() + a.getWidth() / 2;
		float aBottom = a.getWorldY() - a.getHeight() / 2;
		float aTop = a.getWorldY() + a.getHeight() / 2;

		float bLeft = b.getWorldX() - b.getWidth() / 2;
		float bRight = b.getWorldX() + b.getWidth() / 2;
		float bBottom = b.getWorldY() - b.getHeight() / 2;
		float bTop = b.getWorldY() + b.getHeight() / 2;

		return aLeft < bRight &&
			   aRight > bLeft &&
			   aBottom < bTop &&
			   aTop > bBottom;
	}
	
	private static boolean circleRectangle(CircleHitbox circle, RectangleHitbox rectangle) {
		float closestX = Math.max(
			rectangle.getWorldX() - rectangle.getWidth() / 2,
			Math.min(
				circle.getWorldX(),
				rectangle.getWorldX() + rectangle.getWidth() / 2
			)
		);

		float closestY = Math.max(
			rectangle.getWorldY() - rectangle.getHeight() / 2,
			Math.min(
				circle.getWorldY(),
				rectangle.getWorldY() + rectangle.getHeight() / 2
			)
		);

		float dx = circle.getWorldX() - closestX;
		float dy = circle.getWorldY() - closestY;

		return dx * dx + dy * dy < circle.getRadius() * circle.getRadius();
	}
}