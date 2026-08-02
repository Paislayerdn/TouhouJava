package collision;

import entity.Entity;
import entity.Player;
import entity.Bullet;
import game.BulletManager;

import game.GameStats;

public class CollisionSystem {
	private Player player;
	private BulletManager bulletManager;
	
	public CollisionSystem(Player player, BulletManager bulletManager) {
		this.player = player;
		this.bulletManager = bulletManager;
	}
	
	public void update() {
		for (Bullet bullet : bulletManager.getBullets()) {
			CollisionResult collision = check(player, bullet);
			if (collision != null) {
				Hitbox playerHitbox = collision.getFirst();
				Hitbox bulletHitbox = collision.getSecond();
				System.out.printf("[Collision] %s <-hit-> %s\n",
						playerHitbox.getName(), bulletHitbox.getName() );
				player.onHit( playerHitbox, bulletHitbox );
				bullet.onHit( bulletHitbox, playerHitbox );
			}
		}
	}
	
	public static CollisionResult check( Entity a, Entity b ) {
		for (Hitbox ha : a.getHitboxes()) {
			if (!ha.isEnabled()) continue;

			for (Hitbox hb : b.getHitboxes()) {
				if (!hb.isEnabled()) continue;

				if (ha.checkCollision(hb)) return new CollisionResult(ha, hb);
			}
		}

		return null;
	}
}