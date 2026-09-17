package state;

import main.Input;

import entity.Boss;
import entity.Player;
import graphics.Renderer;
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
	public void draw(Renderer renderer) {
		renderer.beginPlayfield();

		boss.draw(renderer);
		BulletManager.draw(renderer);

		if (Debug.isShowHitboxes()) {
			BulletManager.drawHitboxes(renderer);
			boss.drawHitboxes(renderer);
		}

		player.draw(renderer);

		renderer.endPlayfield();

		HUD.draw(renderer);
		Debug.draw(renderer);
	}
}