package game;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

import entity.Bullet;
import collision.Hitbox;

public class BulletManager {
	private final ArrayList<Bullet> bullets = new ArrayList<>();
	private final Object bulletLock = new Object();
	
	public List<Bullet> getBullets() {
		synchronized (bulletLock) {
			return List.copyOf(bullets);
		}
	}
	
	public void add(Bullet bullet) { spawn(bullet); }
	public void spawn(Bullet bullet) {
		synchronized (bulletLock) {
			bullets.add(bullet);
		}
	}

	public void update() {
		synchronized (bulletLock) {
			for (Bullet bullet : bullets) {
				bullet.update();
			}
			bullets.removeIf(bullet -> !bullet.isAlive());
		}
	}

	public void draw(Graphics2D g2) {
		for ( Bullet bullet : getBullets() ) {
			bullet.draw(g2);
		}
	}
	public void drawHitboxes(Graphics2D g2) {
		Color oldColor = g2.getColor();
		g2.setColor(Color.RED);
		for (Bullet bullet : getBullets()) {
			bullet.drawHitboxes(g2);
		}
		
		g2.setColor(oldColor);
	}
	
	public int getBulletCount() {
		synchronized (bulletLock) {
			return bullets.size();
		}
	}	
}