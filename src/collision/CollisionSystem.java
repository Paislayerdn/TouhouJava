package collision;

import entity.Entity;
import entity.Player;
import entity.Bullet;
import entity.Boss;
import game.BulletManager;

import game.GameStats;

public class CollisionSystem {
	private Player player;
	private BulletManager bulletManager;
	private Boss boss;
	
	public CollisionSystem(Player player, BulletManager bulletManager, Boss boss) {
		this.player = player;
		this.bulletManager = bulletManager;
		this.boss = boss;
	}
	
	public void update() {
	for (Bullet bullet : bulletManager.getBullets()) {

		// Boss bullets → Player
		if (bullet.getOwner() == boss) {
			CollisionResult collision = check(player, bullet);

			if (collision != null) {
				Hitbox playerHitbox = collision.getFirst();
				Hitbox bulletHitbox = collision.getSecond();

				player.onHit(playerHitbox, bulletHitbox);
				bullet.onHit(bulletHitbox, playerHitbox);
			}
		}

		// Player bullets → Boss
		if (bullet.getOwner() == player) {
			CollisionResult collision = check(bullet, boss);

			if (collision != null) {
				Hitbox bulletHitbox = collision.getFirst();
				Hitbox bossHitbox = collision.getSecond();

				bullet.onHit(bulletHitbox, bossHitbox);
				boss.onHit(bossHitbox, bulletHitbox);
			}
		}
	}
}
	
	public static CollisionResult check( Entity a, Entity b ) {
		for (Hitbox ha : a.getHitboxes()) {
			if (!ha.isEnabled()) continue;

			for (Hitbox hb : b.getHitboxes()) {
				if (!hb.isEnabled()) continue;

				if (checkHitboxes(ha, hb))
					return new CollisionResult(ha, hb);
			}
		}

		return null;
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