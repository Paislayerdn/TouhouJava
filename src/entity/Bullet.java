package entity;

import java.awt.Color;
import java.awt.Graphics2D;

import game.GameStats;

import static collision.Hitboxes.*;
import collision.Hitbox;

import graphics.Depict;
import resource.ResourceLoader;
import resource.Sound;

public class Bullet extends Entity {
	private final Entity owner;
	private boolean grazable;
	private Sound graze;

	public Bullet() {
		this(null, 999,999);
	}
	public Bullet(Entity owner) {
		this(owner, 999,999);
	}
	public Bullet(Entity owner, double x, double y) {
		this.owner = owner;
		this.x = x;
		this.y = y;
		
		name = "Bullet";
		addHitbox( circleHB(this, "bulletHB", 12) );
		grazable = true;
		graze = ResourceLoader.sound("[TH] Graze");
//		Sound fires = ResourceLoader.sound("[TH] Fires");
//		fires.setVolume(-2);
//		fires.play();
	}

	public Entity getOwner() { return owner; }
	@Override
	public void onHit(Hitbox mine, Hitbox other) {
//		System.out.println("[Bullet] Hit something.");
		if ( "grazeHB".equals(other.getName()) ) {
			onGraze(mine, other);
			return;
		}
		if ( "bossHB".equals(other.getName()) ) {
			alive = false;
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