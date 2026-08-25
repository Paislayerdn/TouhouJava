package game;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

import input.Input;
import main.Settings;

import collision.CollisionSystem;
import entity.Boss;
import entity.Player;

public class Game {
	final private Player player;
	final private Boss boss;
	final private BulletManager bulletManager;
	final private HUD hud;
	final private Debug debug;
	final private CollisionSystem collisionManager;
	
	private boolean lastDebugKey = false;
	
	public void update() {
		if (Input.P && !lastDebugKey) {
			GameStats.debugMode = !GameStats.debugMode;
		}

		lastDebugKey = Input.P;
		
		boss.update();
		bulletManager.update();
		player.update();
		
		collisionManager.update();
		
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
		if (debug.isShowHitboxes()) {
			bulletManager.drawHitboxes(g2);
			boss.drawHitboxes(g2);
		}
		player.draw(g2);
		
		g2.setTransform(oldTransform);
		hud.draw(g2);
		debug.draw(g2);
	}
	
	public Game() {
		bulletManager = new BulletManager();
		player = new Player(bulletManager);
		boss = new Boss(bulletManager, player);
		hud = new HUD();
		debug = new Debug(bulletManager, player, boss);
		collisionManager = new CollisionSystem(player, bulletManager, boss);
	}
}
