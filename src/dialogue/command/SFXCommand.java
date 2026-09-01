package dialogue.command;

import dialogue.DialogueCommand;
import dialogue.DialogueRunner;

public class SFXCommand implements DialogueCommand {
	private final String filename;

	public SFXCommand(String filename) {
		this.filename = filename;
	}

	@Override
	public void execute(DialogueRunner runner) {
		runner.playSound(filename);
	}

	public String getFilename() {
		return filename;
	}
}