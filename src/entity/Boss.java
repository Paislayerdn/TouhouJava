package entity;

import java.awt.Color;
import java.awt.Graphics2D;

import game.BulletManager;

import graphics.Depict;
import resource.ResourceLoader;
import resource.Sound;

import main.Settings;
import spell.Phyllotaxis;
import spell.TestSpell;

public class Boss extends Entity {
	private BulletManager bulletManager;
	private Sound shoot;
	private Player player;
	
	private int timer = 0;

	public Boss(BulletManager bulletManager, Player player) {
		name = "Boss";
		this.bulletManager = bulletManager;
		this.player = player;
		shoot = ResourceLoader.sound("[TH] Shot");

		x = 0;
		y = 120;
	}
	public void setBulletManager(BulletManager bulletManager) { this.bulletManager = bulletManager; }
	public BulletManager getBulletManager() { return bulletManager; }

	@Override
	public void update() {
		updateActions();
		if (timer == 0) {
			shoot.setVolume(-10.0f);
			shoot.play();

			run(new Phyllotaxis(this, player));
		}
		
		timer++;
	}

	@Override
	public void draw(Graphics2D g2) {
		g2.setColor(Color.BLUE);
		Depict.circle(g2, x, y, 40);
	}
}