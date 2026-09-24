package state;

import main.Input;

import action.*;
import dialogue.*;

import entity.Boss;
import entity.Player;
import graphics.Renderer;
import resource.Music;
import resource.ResourceLoader;

import state.gameplay.*;

public class Playing implements GameState {
	private Music bgm;
	private Player player;
	private Boss boss;
	private GameplayScript gameScript;
	private final ActionRunner actions;
	private DialogueRunner dialogueRunner;
	
	private boolean lastDebugKey = false;

	public Playing() {
		player = new Player();
		boss = new Boss(player);
		actions = new ActionRunner();
		
		gameScript = new GameplayScript(this);
		gameScript.start();

		HUD.init();
		Debug.init(player, boss);
		CollisionManager.init(player, boss);
		
		bgm = ResourceLoader.music("PACHAD");
		bgm.setVolume(-15.0f);
		bgm.play();
	}
	
	public void run(Spell spell) {
		actions.add((Action) spell);
		HUD.setSpell(spell);
		
		if (spell.isSpell() && spell.getCaster() != null) {
			HUD.showSCT(spell.getCaster());
		}
	}
	public void run(DialogueRunner dialogueRunner) {
		this.dialogueRunner = dialogueRunner;
	}
	
	@Override
	public void update() {
		if (Input.P && !lastDebugKey) {
			PlayingStats.debugMode = !PlayingStats.debugMode;
		}

		lastDebugKey = Input.P;
		
		actions.update();
		if (dialogueRunner != null) {
			dialogueRunner.update();
		}
		
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

		HUD.drawPlayfield(renderer);

		renderer.endPlayfield();

		HUD.drawOverlay(renderer);
		
		if (dialogueRunner != null) {
			dialogueRunner.draw(renderer);
		}
		
		Debug.draw(renderer);
	}
	public Music getBGM() { return bgm; }
	public Player getPlayer() { return player; }
	public Boss getBoss() { return boss; }
	public GameplayScript getGameScript() { return gameScript; }
	public DialogueRunner getDialogueRunner() { return dialogueRunner; }
}