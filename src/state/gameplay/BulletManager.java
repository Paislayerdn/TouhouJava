package state.gameplay;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

import graphics.Renderer;
import static action.JScratch.*;

import collision.Hitbox;
import static collision.CollisionTag.*;

import entity.Bullet;

public final class BulletManager {
	private final ArrayList<Bullet> playerBullets = new ArrayList<>();
	private final ArrayList<Bullet> enemyBullets = new ArrayList<>();
	final Object bulletLock = new Object();
	private final List<Bullet> playerBulletView = Collections.unmodifiableList(playerBullets);
	private final List<Bullet> enemyBulletView = Collections.unmodifiableList(enemyBullets);
	private final CollisionManager collisionManager;
	
	public BulletManager(CollisionManager collisionManager) {
		this.collisionManager = collisionManager;
	}
	
	public List<Bullet> getPlayerBullets() {
		synchronized (bulletLock) {
			return playerBulletView;
		}
	}
	
	public List<Bullet> getEnemyBullets() {
		synchronized (bulletLock) {
			return enemyBulletView;
		}
	}
	
	public void spawn(Bullet bullet) {
		synchronized (bulletLock) {
			for (Hitbox hb : bullet.getHitboxes()) {
				if (hb.hasTag(PLAYER_BULLET)) {
					playerBullets.add(bullet);
					return;
				} else {
					enemyBullets.add(bullet);
					return;
				}
			}
		}
	}
	public void spawnPlayer(Bullet bullet) {
		synchronized (bulletLock) {
			playerBullets.add(bullet);
		}
	}
	
	public void spawnEnemy(Bullet bullet) {
		synchronized (bulletLock) {
			enemyBullets.add(bullet);
		}
	}

	public void update() {
		synchronized (bulletLock) {
			int playerCount = playerBullets.size();
			int enemyCount = enemyBullets.size();
			for (int i = 0; i < playerCount; i++) {
				playerBullets.get(i).update();
			}
			for (int i = 0; i < enemyCount; i++) {
				Bullet bullet = enemyBullets.get(i);
				bullet.update();
				collisionManager.updateEnemyCandidate(bullet);
			}
			playerBullets.removeIf(bullet -> !bullet.isAlive());
			enemyBullets.removeIf(bullet -> !bullet.isAlive());
		}
	}
	public void clearBomb() {
		synchronized (bulletLock) {
			for (Bullet bullet : enemyBullets) {
				for (Hitbox hitbox : bullet.getHitboxes()) {
					if (hitbox.hasTag(CLEARABLE)) {
						bullet.destroy();
						break;
					}
				}
			}
		}
	}

	public void clearScreen() {
		synchronized (bulletLock) {
			for (Bullet bullet : playerBullets) {
				bullet.run(Sequence(Tween("ghost", 100, 20), Destroy()));
			}
			for (Bullet bullet : enemyBullets) {
				bullet.run(Sequence(Tween("ghost", 100, 20), Destroy()));
			}
		}
	}

	public void draw(Renderer renderer) {
		synchronized (bulletLock) {
			for (Bullet bullet : playerBullets) bullet.draw(renderer);
			for (Bullet bullet : enemyBullets) bullet.draw(renderer);
		}
	}

	public void drawHitboxes(Renderer renderer) {
		synchronized (bulletLock) {
			for (Bullet bullet : playerBullets) bullet.drawHitboxes(renderer);
			for (Bullet bullet : enemyBullets) bullet.drawHitboxes(renderer);
		}
	}
	
	public int getBulletCount() {
		synchronized (bulletLock) {
			return playerBullets.size() + enemyBullets.size();
		}
	}
}
