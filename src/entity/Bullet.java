package entity;

import java.awt.Color;
import java.awt.Graphics2D;

import collision.CircleHitbox;
import collision.Hitbox;
import main.Settings;

import graphics.Depict;

public class Bullet extends Entity {
	private double vx;
	private double vy;

	public Bullet(double x, double y, double vx, double vy) {
		name = "Bullet";
		
		this.x = x;
		this.y = y;

		this.vx = vx;
		this.vy = vy;
		
		addHitbox( new CircleHitbox(this, "bulletHB", 4) );
	}

	@Override
	public void onHit(Hitbox mine, Hitbox other) {
		System.out.println("[Bullet] Hit something.");
	}
	@Override
	public void onGraze(Hitbox mine, Hitbox other) {
		System.out.println("[Bullet] Grazed.");
	}
	
	@Override
	public void update() {
		x += vx;
		y += vy;
	}

	@Override
	public void draw(Graphics2D g2) {
		g2.setColor(Color.WHITE);
		Depict.oval(g2, x, y, 8, 12);
	}
}