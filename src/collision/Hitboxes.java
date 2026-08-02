package collision;

import entity.Entity;

public final class Hitboxes {
	private Hitboxes() {
		// Utility class
	}

	public static CircleHitbox circleHB(Entity owner, String name, double radius) {
		return new CircleHitbox(owner, name, radius);
	}
	public static CircleHitbox circleHB(String name, double radius) {
		return new CircleHitbox(null, name, radius);
	}
	public static CircleHitbox circleHB(Entity owner, double radius) {
		return new CircleHitbox(owner, radius);
	}
}