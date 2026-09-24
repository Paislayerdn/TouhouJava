package state.gameplay;

import java.util.HashSet;
import java.util.Set;

import static collision.CollisionChecker.*;
import static collision.CollisionTag.*;
import collision.CollisionResult;
import collision.Hitbox;

import entity.Player;
import entity.Boss;
import entity.Bullet;

public final class CollisionManager {
	private static final Set<Bullet> playerCandidates = new HashSet<>();
	private static Player player;
	private static Boss boss;
	
	private CollisionManager() {}
	
	public static void init(Player player, Boss boss) {
		CollisionManager.player = player;
		CollisionManager.boss = boss;
	}
	
	public static void updateEnemyCandidate(Bullet bullet) {
		if (!bullet.isAlive()) {
			playerCandidates.remove(bullet);
			return;
		}

		float dx = bullet.getX() - player.getX();
		float dy = bullet.getY() - player.getY();

		float distanceSquared = dx * dx + dy * dy;
		float radiusSquared = 50.0f * 50.0f;

		if (distanceSquared <= radiusSquared) {
			playerCandidates.add(bullet);
		} else {
			playerCandidates.remove(bullet);
		}
	}
	
	public static void update() {
		for (Bullet bullet : BulletManager.getPlayerBullets()) {
			for (Hitbox bulletHitbox : bullet.getHitboxes()) {
				for (Hitbox bossHitbox : boss.getHitboxes()) {
					CollisionResult collision = check(bossHitbox, bulletHitbox);

					if (collision != null) {
						boss.onHit(collision);
						bullet.onHit(collision);
					}
				}
			}
		}

		for (Bullet bullet : playerCandidates) {
			for (Hitbox bulletHitbox : bullet.getHitboxes()) {
				for (Hitbox playerHitbox : player.getHitboxes()) {
					CollisionResult collision = check(playerHitbox, bulletHitbox);

					if (collision != null) {
						player.onHit(collision);
						bullet.onHit(collision);
					}
				}
			}
		}
		
		for (Bullet playerBullet : BulletManager.getPlayerBullets()) {
			for (Hitbox playerBulletHitbox : playerBullet.getHitboxes()) {
				if (!playerBulletHitbox.hasTag(BOMB)) continue;

				for (Bullet enemyBullet : BulletManager.getEnemyBullets()) {
					for (Hitbox enemyBulletHitbox : enemyBullet.getHitboxes()) {
						if (!enemyBulletHitbox.hasTag(CLEARABLE)) continue;

						CollisionResult collision = check(playerBulletHitbox, enemyBulletHitbox);

						if (collision != null) {
							enemyBullet.destroy();
						}
					}
				}
			}
		}
	}
}