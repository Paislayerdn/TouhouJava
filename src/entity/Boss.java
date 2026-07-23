package entity;

import java.awt.Color;
import java.awt.Graphics2D;

import main.Settings;
import game.BulletManager;
import resource.SoundPlayer;

public class Boss extends Entity {
	private BulletManager bulletManager;
	private SoundPlayer fires;
	
	private int timer;

	public Boss(BulletManager bulletManager) {
		this.bulletManager = bulletManager;
		fires = new SoundPlayer("/assets/sounds/[TH] Fires.wav");

		x = Settings.GAME_WIDTH / 2.0;
		y = 120;

	}

	@Override
	public void update() {
		timer++;
		
		if (timer % 10 == 0) {
			
			fires.play();

			bulletManager.spawn(
				new Bullet(x, y, 0, 2.55)
			);

		}
	}

	@Override
	public void draw(Graphics2D g) {

		g.setColor(Color.BLUE);
		g.fillOval((int)x - 20, (int)y - 20, 40, 40);

	}

}