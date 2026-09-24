package dialogue;

import graphics.Renderer;

import action.Action;
import resource.ResourceLoader;

import main.Input;

public final class DialogueRunner {
	private final Dialogue dialogue;

	private int commandIndex;
	private DialogueThing currentSpeaker;

	private String currentText;

	private boolean waitingForInput;
	private Action waitingMovement;
	private boolean previousZ;
	private boolean previousMousePressed;
	
	public DialogueRunner(Dialogue dialogue) {
		this.dialogue = dialogue;
	}
	public DialogueRunner(String name) {
		this( DialogueParser.parse( ResourceLoader.dialogue(name) ) );
	}
	
	public Dialogue getDialogue() { return dialogue; }
	
	public DialogueThing getCurrentSpeaker() { return currentSpeaker; }
	public void setCurrentSpeaker(DialogueThing speaker) { currentSpeaker = speaker; }
	public void removeCurrentSpeaker() {
		if (currentSpeaker == null) return;

		dialogue.removeSpeaker(currentSpeaker.getName());
		currentSpeaker = null;
	}
	public void setCurrentSpeakerName(String displayName) {
		currentSpeaker.setDisplayName(displayName);
	}

	public void waitForMovement(Action action) {
		waitingMovement = action;
	}
	
	public String getCurrentText() { return currentText; }
	public void setCurrentText(String text) { currentText = text; }
	
	public void waitForAdvance() { waitingForInput = true; }

	public void update() {
		for (DialogueThing speaker : dialogue.getSpeakers()) {
			speaker.update();
		}

		if (waitingMovement != null) {
			if (!waitingMovement.isFinished())
				return;

			waitingMovement = null;
		}

		if (waitingForInput) {
			if (!advancePressed()) return;

			waitingForInput = false;
		}

		while (!waitingForInput
				&& waitingMovement == null
				&& commandIndex < dialogue.getCommands().size()) {

			DialogueCommand command =
				dialogue.getCommands().get(commandIndex++);

			command.execute(this);
		}
	}
	
	public void draw(Renderer renderer) {
		for (DialogueThing speaker : dialogue.getSpeakers()) {
			speaker.draw(renderer);
		}
	}
	
	private boolean advancePressed() {
		boolean zPressed = Input.Z && !previousZ;
		boolean mouseClicked = Input.mousePressed && !previousMousePressed;

		previousZ = Input.Z;
		previousMousePressed = Input.mousePressed;

		return zPressed || mouseClicked || Input.PAGEUP;
	}

	public void playSound(String filename) {
		// TODO: Connect to ResourceLoader
	}

	public boolean isFinished() {
		return commandIndex >= dialogue.getCommands().size()
			&& !waitingForInput && waitingMovement == null;
	}

}