package dialogue;

import java.awt.Graphics2D;

import resource.ResourceLoader;

import input.Input;

public class DialogueRunner {
	private final Dialogue dialogue;

	private int commandIndex;
	private DialogueSpeaker currentSpeaker;

	private String currentText;

	private boolean waitingForInput;
	private boolean waitingForMovement;
	private boolean previousZ;
	private boolean previousMousePressed;

	private DialogueMovement movement;

	public DialogueRunner(Dialogue dialogue) {
		this.dialogue = dialogue;
	}
	public DialogueRunner(String name) {
		this( DialogueParser.parse( ResourceLoader.dialogue(name) ) );
	}
	
	public Dialogue getDialogue() { return dialogue; }
	
	public DialogueSpeaker getCurrentSpeaker() { return currentSpeaker; }
	public void setCurrentSpeaker(DialogueSpeaker speaker) { currentSpeaker = speaker; }
	public void removeCurrentSpeaker() {
		if (currentSpeaker == null) return;

		dialogue.removeSpeaker(currentSpeaker.getName());
		currentSpeaker = null;
	}
	public void setCurrentSpeakerName(String displayName) {
		currentSpeaker.setDisplayName(displayName);
	}
	
	public String getCurrentText() { return currentText; }
	public void setCurrentText(String text) { currentText = text; }
	
	public void waitForAdvance() { waitingForInput = true; }

	public void startMovement(DialogueSpeaker speaker,
		double x, double y, int duration, boolean blocking
	) {
		movement = new DialogueMovement(speaker, x, y, duration);

		if (blocking) waitingForMovement = true;
	}

	public void update() {
		updateMovement();

		if (waitingForMovement) return;
		if (waitingForInput) {
			if (!advancePressed()) return;

			waitingForInput = false;
		}

		while (!waitingForInput
				&& !waitingForMovement
				&& commandIndex < dialogue.getCommands().size()) {

			DialogueCommand command = dialogue.getCommands().get(commandIndex++);

			command.execute(this);
		}
	}
	
	public void draw(Graphics2D g2) {
		DialogueRenderer.draw(g2, this);
	}

	private void updateMovement() {
		if (movement == null) return;
		movement.update();

		if (!movement.isFinished()) return;
		movement = null;
		waitingForMovement = false;
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
			&& !waitingForInput && !waitingForMovement;
	}

}