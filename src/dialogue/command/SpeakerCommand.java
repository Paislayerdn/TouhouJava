package dialogue.command;

import main.Debug;
import dialogue.DialogueCommand;
import dialogue.DialogueRunner;
import dialogue.DialogueThing;

public final class SpeakerCommand implements DialogueCommand {
	private final String name;

	public SpeakerCommand(String name) {
		this.name = name;
	}

	@Override
	public void execute(DialogueRunner runner) {
		DialogueThing speaker = runner.getDialogue().getOrCreateSpeaker(name);
		runner.setCurrentSpeaker(speaker);
		Debug.log(this, "Speaker: " + name);
	}
}