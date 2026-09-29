	// FACADE
	package collision;

	import entity.Entity;

	public final class Hitboxes {
		private Hitboxes() {}

		public static CircleHitbox CircleHB(Entity owner, String name, float radius) {
			return new CircleHitbox(owner, name, radius);
		}

		public static RectangleHitbox RectangleHB(
			Entity owner, String name, float width, float height) {
			return new RectangleHitbox(owner, name, width, height);
		}

		public static RectangleHitbox SquareHB(
			Entity owner, String name, float side) {
			return new RectangleHitbox(owner, name, side, side);
		}
	}