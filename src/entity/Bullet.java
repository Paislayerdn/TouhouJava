package entity;

import java.awt.Graphics2D;
import java.awt.Color;

import graphics.Depict;

import game.GameStats;

import resource.ResourceLoader;
import resource.Sound;

import collision.Hitbox;
import static collision.Hitboxes.*;
import collision.CollisionResult;
import static collision.CollisionType.*;

public class Bullet extends Entity {
	private boolean grazable;
	private Sound graze;

	public Bullet() {
		this(999,999);
	}
	public Bullet(double x, double y) {
		this.x = x;
		this.y = y;
		
		name = "Bullet";
		grazable = true;
		graze = ResourceLoader.sound("[TH] Graze");

	}
	
	@Override
	public void onHit(CollisionResult collision) {
		if (collision.getType() == GRAZE) {
			onGraze(
				collision.getSelf(this),
				collision.getOther(this)
			);
			return;
		}

		if (collision.getType() == DAMAGE) {
			alive = false;
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