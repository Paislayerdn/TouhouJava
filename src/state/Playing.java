package state;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

import main.Game;
import main.Input;
import main.Settings;

import entity.Boss;
import entity.Player;
import resource.Music;
import resource.ResourceLoader;
import state.gameplay.PlayingStats;
import state.gameplay.BulletManager;
import state.gameplay.CollisionManager;
import state.gameplay.HUD;
import state.gameplay.Debug;

public class Playing implements GameState {
	private Music bgm;
	private final Player player;
	private final Boss boss;
	
	private boolean lastDebugKey = false;
	
	public Playing() {
		player = new Player();
		boss = new Boss(player);
		HUD.init();
		Debug.init(player, boss);
		CollisionManager.init(player, boss);
		
		bgm = ResourceLoader.music("PACHAD");
		bgm.setVolume(-15.0f);
		bgm.play();
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
		
		CollisionManager.update();
		
		HUD.update();
		Debug.update();
	}
	
	@Override
	public void draw(Graphics2D g2) {
		AffineTransform oldTransform = g2.getTransform();

		// Move from global game space
		// to the playfield's local coordinate space.
		g2.translate(
			Settings.PLAYFIELD_CENTER_X,
			Settings.PLAYFIELD_CENTER_Y
		);

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