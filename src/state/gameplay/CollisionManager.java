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
	private final Set<Bullet> playerCandidates = new HashSet<>();
	private final Player player;
	private final Boss boss;
	private float playerCandidateRadiusSquared = 50.0f * 50.0f;
	
	public CollisionManager(Player player, Boss boss) {
		this.player = player;
		this.boss = boss;
	}
	
	public void setPlayerCandidateRadius(float radius) {
		playerCandidateRadiusSquared = radius * radius;
	}
	
	public void updateEnemyCandidate(Bullet bullet) {
		if (!bullet.isAlive()) {
			playerCandidates.remove(bullet);
			return;
		}

		float dx = bullet.getX() - player.getX();
		float dy = bullet.getY() - player.getY();

		float distanceSquared = dx * dx + dy * dy;
		if (distanceSquared <= playerCandidateRadiusSquared) {
			playerCandidates.add(bullet);
		} else {
			playerCandidates.remove(bullet);
		}
	}
	
	public void update(BulletManager bulletManager) {
		synchronized (bulletManager.bulletLock) {
			var playerBullets = bulletManager.getPlayerBullets();
			var enemyBullets = bulletManager.getEnemyBullets();
			for (Bullet bullet : playerBullets) {
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

			for (Bullet playerBullet : playerBullets) {
				for (Hitbox playerBulletHitbox : playerBullet.getHitboxes()) {
					if (!playerBulletHitbox.hasTag(BOMB)) continue;

					for (Bullet enemyBullet : enemyBullets) {
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
}
