package state;

import main.Input;

import background3d.Background3D;
import background3d.BackgroundObject;

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
	private final DevPanel devPanel;
	private GameplayScript gameScript;
	private final ActionRunner actions;
	private final Background3D background;
	private DialogueRunner dialogueRunner;
	
	private boolean lastDebugKey = false;

	public Playing() {
		actions = new ActionRunner();
		
		hud = new HUD();
		
		player = new Player(hud);
		boss = new Boss(player);
		
		devPanel = new DevPanel(player, boss);
		CollisionManager.init(player, boss);
		
		background = new Background3D();
		
		bgm = ResourceLoader.music("PACHAD");
		bgm.setVolume(-15.0f);
		bgm.play();
		
		BackgroundObject object;
		object = new BackgroundObject(-60, 0, 150, 200, 200);
		object.appearance.setCostume(
			ResourceLoader.image("Icon")
		);
		object.setRotation(0, 90, 0);
		background.add(object);
		object = new BackgroundObject(0, 0, 150, 200, 200);
		object.appearance.setCostume(
			ResourceLoader.image("Icon")
		);
		object.setRotation(0, 0, 90);
		background.add(object);
		object = new BackgroundObject(60, 0, 150, 200, 200);
		object.appearance.setCostume(
			ResourceLoader.image("Icon")
		);
		object.setRotation(90, 0, 0);
		background.add(object);


		background.getCamera().setXYZ(0, 30, 0);
		background.getCamera().setRotation(0, 0, 0);
		
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
		background.getCamera().changeRotation(1f,1f,1f);
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
		devPanel.update();
	}
	
	@Override
	public void draw(Renderer renderer) {
		renderer.beginPlayfield();
		
		renderer.background3D(background);

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