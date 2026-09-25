package dialogue.command;

import main.Debug;
import dialogue.DialogueCommand;
import dialogue.DialogueRunner;

import resource.ResourceLoader;

public final class ExpressionCommand implements DialogueCommand {
	private final String expression;

	public ExpressionCommand(String expression) {
		this.expression = expression;
	}

	@Override
	public void execute(DialogueRunner runner) {
		runner.getCurrentSpeaker()
			.getAppearance()
			.setCostume(ResourceLoader.image(expression));

		Debug.log(this, "Expression: " + expression);
	}
}