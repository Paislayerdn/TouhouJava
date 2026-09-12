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
		if (a instanceof CircleHitbox &&
			b instanceof CircleHitbox) {
			CircleHitbox ca = (CircleHitbox) a;
			CircleHitbox cb = (CircleHitbox) b;

			return circleCircle(ca, cb);
		}

		if (a instanceof CircleHitbox &&
			b instanceof RectangleHitbox) {
			CircleHitbox ca = (CircleHitbox) a;
			RectangleHitbox rb = (RectangleHitbox) b;

			return circleRectangle(ca, rb);
		}

		if (a instanceof RectangleHitbox &&
			b instanceof CircleHitbox) {
			RectangleHitbox ra = (RectangleHitbox) a;
			CircleHitbox cb = (CircleHitbox) b;

			return circleRectangle(cb, ra);
		}

		if (a instanceof RectangleHitbox &&
			b instanceof RectangleHitbox) {
			RectangleHitbox ra = (RectangleHitbox) a;
			RectangleHitbox rb = (RectangleHitbox) b;

			return rectangleRectangle(ra, rb);
		}

		return false;
	}
	
	private static boolean circleCircle(CircleHitbox a, CircleHitbox b) {
		double dx = a.getWorldX() - b.getWorldX();
		double dy = a.getWorldY() - b.getWorldY();

		double distanceSquared = dx * dx + dy * dy;
		double radiusSum = a.getRadius() + b.getRadius();

		return distanceSquared < radiusSum * radiusSum;
	}
	
	private static boolean rectangleRectangle(RectangleHitbox a, RectangleHitbox b) {
		double aLeft = a.getWorldX() - a.getWidth() / 2;
		double aRight = a.getWorldX() + a.getWidth() / 2;
		double aBottom = a.getWorldY() - a.getHeight() / 2;
		double aTop = a.getWorldY() + a.getHeight() / 2;

		double bLeft = b.getWorldX() - b.getWidth() / 2;
		double bRight = b.getWorldX() + b.getWidth() / 2;
		double bBottom = b.getWorldY() - b.getHeight() / 2;
		double bTop = b.getWorldY() + b.getHeight() / 2;

		return aLeft < bRight &&
			   aRight > bLeft &&
			   aBottom < bTop &&
			   aTop > bBottom;
	}
	
	private static boolean circleRectangle(CircleHitbox circle, RectangleHitbox rectangle) {
		double closestX = Math.max(
			rectangle.getWorldX() - rectangle.getWidth() / 2,
			Math.min(
				circle.getWorldX(),
				rectangle.getWorldX() + rectangle.getWidth() / 2
			)
		);

		double closestY = Math.max(
			rectangle.getWorldY() - rectangle.getHeight() / 2,
			Math.min(
				circle.getWorldY(),
				rectangle.getWorldY() + rectangle.getHeight() / 2
			)
		);

		double dx = circle.getWorldX() - closestX;
		double dy = circle.getWorldY() - closestY;

		return dx * dx + dy * dy < circle.getRadius() * circle.getRadius();
	}
}