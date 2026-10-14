package state;

import main.Input;
import main.Game;

import background3d.Background3D;

import action.*;
import dialogue.*;

import entity.Boss;
import entity.Player;
import graphics.Renderer;
import resource.Music;
import resource.ResourceLoader;

import state.gameplay.*;

public class Playing extends GameState {
	private Music bgm;
	private Player player;
	private Boss boss;
	private final HUD hud;
	private final DevPanel devPanel;
	private GameplayScript gameScript;
	private final ActionRunner actions;
	private final Background3D background;
	private DialogueRunner dialogueRunner;

	public Playing(Game game) {
		super(game);
		actions = new ActionRunner();
		
		hud = new HUD();
		
		player = new Player(hud);
		boss = new Boss(player);
		
		devPanel = new DevPanel(player, boss);
		CollisionManager.init(player, boss);
		
		background = new Background3D();
		
		bgm = ResourceLoader.music("PACHAD");
		bgm.setVolume(-25.0f);
		bgm.play();
		
		background.load("test1");
		background.event("init");
		
		gameScript = new GameplayScript(this);
		gameScript.start();
	}
	
	public void spawnBoss(int maxHP) {
		boss.setMaxHP(maxHP);
	}
	public void run(Spell spell) {
		actions.add((Action) spell);
		if (spell.getCaster().equalsIgnoreCase("LAMBDA")) hud.setCurrentSpell(spell);
		
		if (spell.isSpell() && spell.getCaster() != null) {
			hud.showSCT(spell.getCaster());
		}
	}
	public void converse(String file) {
		this.dialogueRunner = new DialogueRunner(this, file);
	}
	
	@Override
	public void update() {
		if (Input.tap.P) {PlayingStats.debugMode = !PlayingStats.debugMode;}
		
		actions.update();
		if (dialogueRunner != null) {
			dialogueRunner.update();
		}
		
		boss.update();
		player.update();
		BulletManager.update();
		
		CollisionManager.update();
		
		gameScript.update();
		
		hud.update();
		devPanel.update();
	}
	
	@Override
	public void draw(Renderer renderer) {
		renderer.beginPlayfield();
		
		background.render(renderer);

		boss.draw(renderer);
		player.draw(renderer);
		BulletManager.draw(renderer);

		if (devPanel.isShowHitboxes()) {
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
		
		devPanel.draw(renderer);
	}
	
	public Music getBGM() { return bgm; }
	public Player getPlayer() { return player; }
	public Boss getBoss() { return boss; }
	public GameplayScript getGameScript() { return gameScript; }
	public DialogueRunner getDialogueRunner() { return dialogueRunner; }
	public Background3D getBackground() { return background; }
}
