package entity;

import java.awt.Color;
import java.awt.Graphics2D;

import game.BulletManager;
import collision.Hitbox;

import graphics.Depict;
import resource.ResourceLoader;
import resource.Music;

import static collision.Hitboxes.*;
import spell.Phyllotaxis;
import spell.TestSpell;

public class Boss extends Entity {
	private BulletManager bulletManager;
	private Music ost;
	private Player player;
	
	private int timer = 0;

	public Boss(BulletManager bulletManager, Player player) {
		name = "Boss";
		this.bulletManager = bulletManager;
		this.player = player;
		x = 0;
		y = 120;
		this.ost = ResourceLoader.music("PACHAD");
		ost.setVolume(-17.5f);
		
		addHitbox(rectangleHB(this, "bossHB", 85, 90));
	}
	public void setBulletManager(BulletManager bulletManager) { this.bulletManager = bulletManager; }
	public BulletManager getBulletManager() { return bulletManager; }

//	@Override
//	public void onHit(Hitbox mine, Hitbox other) {
//		System.out.println("[Boss] Hit by " + other.getOwner().getName());
//	}

	@Override
	public void update() {
		updateActions();
		if (timer == 0) {
			ost.play();
			run(new Phyllotaxis(this, player));
		}
		
		timer++;
	}

	@Override
	public void draw(Graphics2D g2) {
		g2.setColor(Color.BLUE);
		Depict.circle(g2, x, y, 80);
	}
}