package game;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

import input.Input;
import entity.Boss;
import entity.Player;

import main.Settings;

import static action.JScratch.*;


public class Game {
	final private Player player;
	final private Boss boss;
	final private BulletManager bulletManager;
	final private HUD hud;
	final private Debug debug;
	
	private boolean lastDebugKey = false;
	
	public void update() {
		if (Input.debug && !lastDebugKey) {
			GameStats.debugMode = !GameStats.debugMode;
		}

		lastDebugKey = Input.debug;
		
		boss.update();
		bulletManager.update();
		player.update();
		hud.update();
		debug.update();
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
		hud.draw(g2);
		debug.draw(g2);
	}
	
	public Game() {
		player = new Player();
		bulletManager = new BulletManager();
		boss = new Boss(bulletManager);
		hud = new HUD();
		debug = new Debug(player);
	}
	
}
