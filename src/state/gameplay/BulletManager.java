package state.gameplay;

import graphics.Renderer;

import java.util.List;
import java.util.ArrayList;

import entity.Bullet;

public final class BulletManager {
	private static final ArrayList<Bullet> playerBullets = new ArrayList<>();
	private static final ArrayList<Bullet> enemyBullets = new ArrayList<>();
	private static final Object bulletLock = new Object();
	
	private BulletManager() {}
	
	public static List<Bullet> getPlayerBullets() {
		synchronized (bulletLock) {
			return List.copyOf(playerBullets);
		}
	}
	
	public static List<Bullet> getEnemyBullets() {
		synchronized (bulletLock) {
			return List.copyOf(enemyBullets);
		}
	}
	
	public static void spawnPlayer(Bullet bullet) {
		synchronized (bulletLock) {
			playerBullets.add(bullet);
		}
	}
	
	public static void spawnEnemy(Bullet bullet) {
		synchronized (bulletLock) {
			enemyBullets.add(bullet);
		}
	}

	public static void update() {
		List<Bullet> playerSnapshot;
		List<Bullet> enemySnapshot;

		synchronized (bulletLock) {
			playerSnapshot = List.copyOf(playerBullets);
			enemySnapshot = List.copyOf(enemyBullets);
		}

		for (Bullet bullet : playerSnapshot) {
			bullet.update();
		}

		for (Bullet bullet : enemySnapshot) {
			bullet.update();
			CollisionManager.updateEnemyCandidate(bullet);
		}

		synchronized (bulletLock) {
			playerBullets.removeIf(bullet -> !bullet.isAlive());
			enemyBullets.removeIf(bullet -> !bullet.isAlive());
		}
	}

	public static void draw(Renderer renderer) {
		for (Bullet bullet : getPlayerBullets()) {
			bullet.draw(renderer);
		}

		for (Bullet bullet : getEnemyBullets()) {
			bullet.draw(renderer);
		}
	}

	public static void drawHitboxes(Renderer renderer) {
		for (Bullet bullet : getPlayerBullets()) {
			bullet.drawHitboxes(renderer);
		}

		for (Bullet bullet : getEnemyBullets()) {
			bullet.drawHitboxes(renderer);
		}
	}
	
	public static int getBulletCount() {
		synchronized (bulletLock) {
			return playerBullets.size() + enemyBullets.size();
		}
	}
}