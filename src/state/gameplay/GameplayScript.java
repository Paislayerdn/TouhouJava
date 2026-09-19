package state.gameplay;

import action.Action;
import action.ActionContext;
import action.ActionRunner;
import dialogue.DialogueRunner;

import entity.Boss;
import entity.Player;

import spell.*;

public final class GameplayScript {
	private final Player player;
	private final Boss boss;
	
	private final ActionRunner actions;
	private DialogueRunner dialogueRunner;

	public GameplayScript(Player player, Boss boss) {
		this.player = player;
		this.boss = boss;
		this.actions = new ActionRunner();
	}

	public void start() {
//		this.run(new Phyllotaxis(boss, player));
		this.run(new LuaSpell(boss, player, "Eirin"));
//		dialogueRunner = new DialogueRunner("Test");
	}
	public void run(Action action) {
		actions.add(action);
	}

	public void update() {
		actions.update();
		if (dialogueRunner != null) dialogueRunner.update();
	}

	public ActionContext getActionContext() {
		return actions.getContext();
	}
	public void setDialogueRunner(DialogueRunner dialogueRunner) { this.dialogueRunner = dialogueRunner; }
	public DialogueRunner getDialogueRunner() { return dialogueRunner; }
	public Player getPlayer() { return player; } 
	public Boss getBoss() { return boss; }
}	