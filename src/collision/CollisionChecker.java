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
		float angleA = a.getOwner().getOrientation().getAngle();
		float angleB = b.getOwner().getOrientation().getAngle();
		if (angleA != 0.0f || angleB != 0.0f) {
			return orientedRectangleRectangle(a, b, angleA, angleB);
		}

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
		float angle = rectangle.getOwner().getOrientation().getAngle();
		float dx = circle.getWorldX() - rectangle.getWorldX();
		float dy = circle.getWorldY() - rectangle.getWorldY();
		float circleX = dx;
		float circleY = dy;
		if (angle != 0.0f) {
			float radians = (float) Math.toRadians(angle);
			float cos = (float) Math.cos(radians);
			float sin = (float) Math.sin(radians);
			circleX = dx * cos + dy * sin;
			circleY = -dx * sin + dy * cos;
		}

		float closestX = Math.max(
			-rectangle.getWidth() / 2,
			Math.min(
				circleX,
				rectangle.getWidth() / 2
			)
		);

		float closestY = Math.max(
			-rectangle.getHeight() / 2,
			Math.min(
				circleY,
				rectangle.getHeight() / 2
			)
		);

		dx = circleX - closestX;
		dy = circleY - closestY;

		return dx * dx + dy * dy < circle.getRadius() * circle.getRadius();
	}

	private static boolean orientedRectangleRectangle(RectangleHitbox a, RectangleHitbox b, float angleA, float angleB) {
		float radiansA = (float) Math.toRadians(angleA);
		float radiansB = (float) Math.toRadians(angleB);
		float cosA = (float) Math.cos(radiansA), sinA = (float) Math.sin(radiansA);
		float cosB = (float) Math.cos(radiansB), sinB = (float) Math.sin(radiansB);
		float dx = b.getWorldX() - a.getWorldX(), dy = b.getWorldY() - a.getWorldY();
		float halfWA = a.getWidth() / 2, halfHA = a.getHeight() / 2;
		float halfWB = b.getWidth() / 2, halfHB = b.getHeight() / 2;
		return overlapsAxis(dx, dy, cosA, sinA, halfWA, halfHA, halfWB, halfHB, cosB, sinB)
			&& overlapsAxis(dx, dy, -sinA, cosA, halfHA, halfWA, halfWB, halfHB, cosB, sinB)
			&& overlapsAxis(dx, dy, cosB, sinB, halfWB, halfHB, halfWA, halfHA, cosA, sinA)
			&& overlapsAxis(dx, dy, -sinB, cosB, halfHB, halfWB, halfWA, halfHA, cosA, sinA);
	}

	private static boolean overlapsAxis(float dx, float dy, float axisX, float axisY,
		float ownHalfX, float ownHalfY, float otherHalfX, float otherHalfY,
		float otherCos, float otherSin) {
		float distance = Math.abs(dx * axisX + dy * axisY);
		float ownRadius = ownHalfX;
		// Projection of the other box's two local axes onto this axis.
		float otherRadius = otherHalfX * Math.abs(otherCos * axisX + otherSin * axisY)
			+ otherHalfY * Math.abs(-otherSin * axisX + otherCos * axisY);
		return distance < ownRadius + otherRadius;
	}
}
