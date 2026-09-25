package dialogue.command;

import dialogue.DialogueCommand;
import dialogue.DialogueDebug;
import dialogue.DialogueLexer;
import dialogue.DialogueRunner;

public final class TextCommand implements DialogueCommand {
	private final String text;

	public TextCommand(String text) {
		this.text = text;
	}

	@Override
	public void execute(DialogueRunner runner) {
		String resolved = DialogueLexer.substitute(runner, text);

		runner.setCurrentText(resolved);
		runner.waitForAdvance();

		DialogueDebug.log(this, "Text: " + resolved);
	}

	public String getText() { return text; }
}