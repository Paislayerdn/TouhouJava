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
	private final HUD hud;
	private final Debug debug;
	private GameplayScript gameScript;
	private final ActionRunner actions;
	private DialogueRunner dialogueRunner;
	
	private boolean lastDebugKey = false;

	public Playing() {
		actions = new ActionRunner();
		
		hud = new HUD();
		
		player = new Player(hud);
		boss = new Boss(player);
		
		debug = new Debug(player, boss);
		CollisionManager.init(player, boss);
		
		bgm = ResourceLoader.music("PACHAD");
		bgm.setVolume(-15.0f);
		bgm.play();
		
		gameScript = new GameplayScript(this);
		gameScript.start();
	}
	
	public void run(Spell spell) {
		actions.add((Action) spell);
		if (spell.getCaster() == "LAMBDA") hud.setCurrentSpell(spell);
		
		if (spell.isSpell() && spell.getCaster() != null) {
			hud.showSCT(spell.getCaster());
		}
	}
	public void converse(String file) {
		this.dialogueRunner = new DialogueRunner(this, file);
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
		
		hud.update();
		debug.update();
	}
	
	@Override
	public void draw(Renderer renderer) {
		renderer.beginPlayfield();

		boss.draw(renderer);
		player.draw(renderer);
		BulletManager.draw(renderer);

		if (debug.isShowHitboxes()) {
			BulletManager.drawHitboxes(renderer);
			boss.drawHitboxes(renderer);
			player.drawHitboxes(renderer);
		}

		hud.drawPlayfield(renderer);

		renderer.endPlayfield();

		hud.drawOverlay(renderer);
		
		if (dialogueRunner != null) {
			dialogueRunner.draw(renderer);
		}
		
		debug.draw(renderer);
	}
	public Music getBGM() { return bgm; }
	public Player getPlayer() { return player; }
	public Boss getBoss() { return boss; }
	public GameplayScript getGameScript() { return gameScript; }
	public DialogueRunner getDialogueRunner() { return dialogueRunner; }
}