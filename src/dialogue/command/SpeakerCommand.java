package dialogue.command;

import dialogue.DialogueCommand;
import dialogue.DialogueDebug;
import dialogue.DialogueRunner;
import dialogue.DialogueSpeaker;

public class SpeakerCommand implements DialogueCommand {
	private final String name;

	public SpeakerCommand(String name) {
		this.name = name;
	}

	@Override
	public void execute(DialogueRunner runner) {
		DialogueSpeaker speaker = runner.getDialogue().getOrCreateSpeaker(name);
		runner.setCurrentSpeaker(speaker);
		DialogueDebug.log(this, "Speaker: " + name);
	}
}