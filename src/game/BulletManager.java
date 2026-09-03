package game;

import java.util.ArrayList;
import java.util.List;
import java.awt.Graphics2D;
import java.awt.Color;

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
		synchronized (bulletLock) {
			for (Bullet bullet : bullets) {
				bullet.update();
			}
			bullets.removeIf(bullet -> !bullet.isAlive());
		}
	}

	public static void draw(Graphics2D g2) {
		for ( Bullet bullet : getBullets() ) {
			bullet.draw(g2);
		}
	}
	public static void drawHitboxes(Graphics2D g2) {
		Color oldColor = g2.getColor();
		g2.setColor(Color.RED);
		for (Bullet bullet : getBullets()) {
			bullet.drawHitboxes(g2);
		}
		
		g2.setColor(oldColor);
	}
	
	public static int getBulletCount() {
		synchronized (bulletLock) {
			return bullets.size();
		}
	}	
}