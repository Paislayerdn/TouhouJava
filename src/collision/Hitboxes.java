// FACADE
package collision;

import entity.Entity;

public final class Hitboxes {
	// Utility class
	private Hitboxes() {}

	public static CircleHitbox circleHB(Entity owner, String name, double radius) {
		return new CircleHitbox(owner, name, radius);
	}
	public static CircleHitbox circleHB(Entity owner, double radius) {
		return new CircleHitbox(owner, radius);
	}
//	public static CircleHitbox circleHB(String name, double radius) {
//		return new CircleHitbox(null, name, radius);
//	}
	
	public static RectangleHitbox rectangleHB(
		Entity owner, String name, double width, double height) {
		return new RectangleHitbox(owner, name, width, height);
	}
	public static RectangleHitbox rectangleHB(
		Entity owner, double width, double height) {
		return new RectangleHitbox(owner, width, height);
	}
//	public static RectangleHitbox rectangleHB(
//		String name, double width, double height) {
//		return new RectangleHitbox(null, name, width, height);
//	}
	public static RectangleHitbox squareHB(
		Entity owner, String name, double side) {
		return new RectangleHitbox(owner, name, side, side);
	}
}