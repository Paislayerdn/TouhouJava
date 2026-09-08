package state.gameplay;

import static collision.CollisionChecker.*;
import collision.CollisionResult;
import collision.Hitbox;

import entity.Player;
import entity.Boss;
import entity.Bullet;
import state.gameplay.BulletManager;

public final class CollisionManager {
	private static Player player;
	private static Boss boss;
	
	private CollisionManager() {}
	
	public static void init(Player player, Boss boss) {
		CollisionManager.player = player;
		CollisionManager.boss = boss;
	}
	
	public static void update() {
		for (Bullet bullet : BulletManager.getBullets()) {
			for (Hitbox bulletHitbox : bullet.getHitboxes()) {
				for (Hitbox playerHitbox : player.getHitboxes()) {
					CollisionResult collision = check(playerHitbox, bulletHitbox);

					if (collision != null) {
						player.onHit(collision);
						bullet.onHit(collision);
					}
				}

				for (Hitbox bossHitbox : boss.getHitboxes()) {
					CollisionResult collision = check(bossHitbox, bulletHitbox);

					if (collision != null) {
						boss.onHit(collision);
						bullet.onHit(collision);
					}
				}
			}
		}
	}
}