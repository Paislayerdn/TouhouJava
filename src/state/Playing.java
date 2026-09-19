package state;

import main.Input;

import entity.Boss;
import entity.Player;
import graphics.Renderer;
import resource.Music;
import resource.ResourceLoader;

import state.gameplay.*;

public class Playing implements GameState {
	private Music bgm;
	private final Player player;
	private final Boss boss;
	private final GameplayScript gameScript;
	
	private boolean lastDebugKey = false;
	
	public Playing() {
		player = new Player();
		boss = new Boss(player);
		
		gameScript = new GameplayScript(player, boss);
		gameScript.start();

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
		
		gameScript.update();
		
		boss.update();
		player.update();
		BulletManager.update();
		
		CollisionManager.update();
		
		HUD.update();
		Debug.update();
	}
	
	@Override
	public void draw(Renderer renderer) {
		renderer.beginPlayfield();

		boss.draw(renderer);
		player.draw(renderer);
		BulletManager.draw(renderer);

		if (Debug.isShowHitboxes()) {
			BulletManager.drawHitboxes(renderer);
			boss.drawHitboxes(renderer);
			player.drawHitboxes(renderer);
		}


		renderer.endPlayfield();

		HUD.draw(renderer);
		Debug.draw(renderer);
	}
}