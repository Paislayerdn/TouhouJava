package entity;

import java.awt.Graphics2D;
import java.awt.Color;

import graphics.Depict;

import game.BulletManager;

import resource.ResourceLoader;
import resource.Sound;
import resource.Music;

import collision.Hitbox;
import static collision.Hitboxes.*;
import spell.LuaSpell;
import spell.Phyllotaxis;
import spell.TestSpell;

public class Boss extends Entity {
	private BulletManager bulletManager;
	private Music ost;
	private Sound lowHP;
	private Player player;
	
	private double maxHP=0;
	private double hp=0;
	
	private int timer = 0;

	public Boss(BulletManager bulletManager, Player player) {
		name = "Boss";
		this.bulletManager = bulletManager;
		this.player = player;
		x = 0;
		y = 120;
		this.ost = ResourceLoader.music("PACHAD");
		ost.setVolume(-22.5f);
		this.lowHP = ResourceLoader.sound("[TH] LowHP");
		lowHP.setVolume(-10.0f);
		
		addHitbox(rectangleHB(this, "bossHB", 85, 90));
	}
	public void setMaxHP(int maxHP) {
		this.maxHP = maxHP;
		this.hp = maxHP;
	}

	public double getMaxHP() { return maxHP; }
	public double getHP() { return hp; }
	public void damage(double amount) {
		if (hp < 0) { hp = 0; }
		else {
			hp -= amount;
			if (hp/maxHP<0.15) lowHP.play();
			System.out.println(hp);
		}
	}
	
	public void setBulletManager(BulletManager bulletManager) { this.bulletManager = bulletManager; }
	public BulletManager getBulletManager() { return bulletManager; }

	@Override
	public void onHit(Hitbox mine, Hitbox other) {
		damage(1);
	}

	@Override
	public void update() {
		updateActions();
		if (timer == 0) {
			ost.play();
			run(new LuaSpell(this, player, "GoldenLua"));
		}
		
		timer++;
	}

	@Override
	public void draw(Graphics2D g2) {
		g2.setColor(Color.BLUE);
		Depict.circle(g2, x, y, 80);
	}
}