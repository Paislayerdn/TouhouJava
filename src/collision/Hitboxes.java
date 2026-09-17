// FACADE
package collision;

import entity.Entity;

public final class Hitboxes {
	// Utility class
	private Hitboxes() {}

	public static CircleHitbox circleHB(Entity owner, String name, float radius) {
		return new CircleHitbox(owner, name, radius);
	}
	public static CircleHitbox circleHB(Entity owner, float radius) {
		return new CircleHitbox(owner, radius);
	}
//	public static CircleHitbox circleHB(String name, float radius) {
//		return new CircleHitbox(null, name, radius);
//	}
	
	public static RectangleHitbox rectangleHB(
		Entity owner, String name, float width, float height) {
		return new RectangleHitbox(owner, name, width, height);
	}
	public static RectangleHitbox rectangleHB(
		Entity owner, float width, float height) {
		return new RectangleHitbox(owner, width, height);
	}
//	public static RectangleHitbox rectangleHB(
//		String name, float width, float height) {
//		return new RectangleHitbox(null, name, width, height);
//	}
	public static RectangleHitbox squareHB(
		Entity owner, String name, float side) {
		return new RectangleHitbox(owner, name, side, side);
	}
}