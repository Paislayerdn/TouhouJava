package entity;

import java.awt.Color;
import java.awt.Graphics2D;

import main.Settings;
import game.BulletManager;

import graphics.Depict;
import resource.ResourceLoader;
import resource.Sound;

public class Boss extends Entity {
	private BulletManager bulletManager;
	private Sound fires;
	
	private int timer = 0;

	public Boss(BulletManager bulletManager) {
		this.bulletManager = bulletManager;
		fires = ResourceLoader.sound("[TH] Fires");

		x = 0;
		y = 120;
	}

	@Override
	public void update() {
		if (timer % 15 == 0) {
			fires.setVolume(-10.0f);
			fires.play();

			bulletManager.spawn(
				new Bullet(x, y, 0, -2.55)
			);

		}
		
		timer++;
	}

	@Override
	public void draw(Graphics2D g2) {
		g2.setColor(Color.BLUE);
		Depict.circle(g2, x, y, 40);
	}
}