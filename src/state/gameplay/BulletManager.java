package state.gameplay;

import graphics.Renderer;

import java.util.List;
import java.util.ArrayList;

import entity.Bullet;

public final class BulletManager {
	private static final ArrayList<Bullet> bullets = new ArrayList<>();
	private static final Object bulletLock = new Object();
	
	private BulletManager() {}
	
	public static List<Bullet> getBullets() {
		synchronized (bulletLock) {
			return List.copyOf(bullets);
		}
	}
	
	public static void add(Bullet bullet) { spawn(bullet); }
	public static void spawn(Bullet bullet) {
		synchronized (bulletLock) {
			bullets.add(bullet);
		}
	}

	public static void update() {
		List<Bullet> snapshot;

		synchronized (bulletLock) {
			snapshot = List.copyOf(bullets);
		}

		for (Bullet bullet : snapshot) {
			bullet.update();
		}

		synchronized (bulletLock) {
			bullets.removeIf(bullet -> !bullet.isAlive());
		}
	}

	public static void draw(Renderer renderer) {
		for ( Bullet bullet : getBullets() ) {
			bullet.draw(renderer);
		}
	}
	public static void drawHitboxes(Renderer renderer) {
		for (Bullet bullet : getBullets()) {
			bullet.drawHitboxes(renderer);
		}
	}
	
	public static int getBulletCount() {
		synchronized (bulletLock) {
			return bullets.size();
		}
	}	
}