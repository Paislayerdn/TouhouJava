package dialogue.command;

import dialogue.DialogueCommand;
import dialogue.DialogueDebug;
import dialogue.DialogueRunner;

public class TextCommand implements DialogueCommand {
	private final String text;

	public TextCommand(String text) {
		this.text = text;
	}

	@Override
	public void execute(DialogueRunner runner) {
		runner.setCurrentText(text);
		runner.waitForAdvance();
		DialogueDebug.log(this, "Text: " + text);
	}

	public String getText() {
		return text;
	}
}