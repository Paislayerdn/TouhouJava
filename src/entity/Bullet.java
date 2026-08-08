package entity;

import java.awt.Color;
import java.awt.Graphics2D;

import static collision.Hitboxes.*;
import collision.Hitbox;
import game.GameStats;


import graphics.Depict;
import resource.ResourceLoader;
import resource.Sound;

public class Bullet extends Entity {
	private boolean grazable;
	private Sound graze;

	public Bullet() {
		this(999,999);
	}
	public Bullet(double x, double y) {
		name = "Bullet";
		
		this.x = x;
		this.y = y;
		
		addHitbox( circleHB(this, "bulletHB", 12) );
		grazable = true;
		graze = ResourceLoader.sound("[TH] Graze");
//		Sound fires = ResourceLoader.sound("[TH] Fires");
//		fires.setVolume(-2);
//		fires.play();
	}

	@Override
	public void onHit(Hitbox mine, Hitbox other) {
//		System.out.println("[Bullet] Hit something.");
		if ( "grazeHB".equals(other.getName()) ) {
			onGraze(mine, other);
			return;
		}
	}
	
	public void onGraze(Hitbox mine, Hitbox other) {
		if (grazable) {
//			System.out.println("[Bullet] Grazed.");
			GameStats.addGraze();
			graze.setVolume(0);
			graze.play();
			grazable = false;
		} else {
//			System.out.println("[Bullet] Already grazed.");
		}
	}
	
	@Override
	public void update() {
		updateActions();
	}

	@Override
	public void draw(Graphics2D g2) {
		g2.setColor(Color.WHITE);
		Depict.oval(g2, x, y, 24, 12, trueAngle);
	}
}