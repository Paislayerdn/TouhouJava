package game;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

import entity.Player;
import entity.Boss;
import main.Settings;

public class Game {
	
	private Player player;
	private Boss boss;
	private BulletManager bulletManager;
	private GUI gui;
	
	public void update() {
		boss.update();
		bulletManager.update();
		player.update();
		gui.update();
	}
	
	public void draw(Graphics2D g2) {
		AffineTransform oldTransform = g2.getTransform();
		
		g2.translate(
			Settings.PLAYFIELD_X + Settings.PLAYFIELD_WIDTH / 2,
			Settings.PLAYFIELD_Y + Settings.PLAYFIELD_HEIGHT / 2
		);
		g2.scale(1, -1);
		
		boss.draw(g2);
		bulletManager.draw(g2);
		player.draw(g2);
		
		g2.setTransform(oldTransform);
		gui.draw(g2);
	}
	
	public Game() {
		player = new Player();
		bulletManager = new BulletManager();
		boss = new Boss(bulletManager);
		gui = new GUI();
	}
	
}
