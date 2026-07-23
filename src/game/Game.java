package game;

import java.awt.Graphics2D;

import entity.Player;
import entity.Boss;

public class Game {
	private Player player;
	private Boss boss;
	private BulletManager bulletManager;
	
	public void update() {
		boss.update();
		bulletManager.update();
		player.update();
	}
	
	public void draw(Graphics2D g2) {
		boss.draw(g2);
		bulletManager.draw(g2);
		player.draw(g2);
	}
	
	public Game() {
		player = new Player();
		bulletManager = new BulletManager();
		boss = new Boss(bulletManager);
	}
	
}
