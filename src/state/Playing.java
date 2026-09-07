package state;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

import main.Game;
import main.Input;
import main.Settings;

import collision.CollisionSystem;
import entity.Boss;
import entity.Player;
import gameplay.BulletManager;
import gameplay.PlayingStats;
import gameplay.HUD;
import gameplay.Debug;

public class Playing implements GameState {
	private final Player player;
	private final Boss boss;
	
	private boolean lastDebugKey = false;
	
	public Playing() {
		player = new Player();
		boss = new Boss(player);
		HUD.init();
		Debug.init(player, boss);
		CollisionSystem.init(player, boss);
	}
	
	@Override
	public void update() {
		if (Input.P && !lastDebugKey) {
			PlayingStats.debugMode = !PlayingStats.debugMode;
		}

		lastDebugKey = Input.P;
		
		boss.update();
		BulletManager.update();
		player.update();
		
		CollisionSystem.update();
		
		HUD.update();
		Debug.update();
	}
	
	@Override
	public void draw(Graphics2D g2) {
		AffineTransform oldTransform = g2.getTransform();
		
		g2.translate(
			Settings.PLAYFIELD_X + Settings.PLAYFIELD_WIDTH / 2,
			Settings.PLAYFIELD_Y + Settings.PLAYFIELD_HEIGHT / 2
		);
		g2.scale(1, -1);
		
		boss.draw(g2);
		BulletManager.draw(g2);
		if (Debug.isShowHitboxes()) {
			BulletManager.drawHitboxes(g2);
			boss.drawHitboxes(g2);
		}
		player.draw(g2);
		
		g2.setTransform(oldTransform);
		HUD.draw(g2);
		Debug.draw(g2);
	}
}