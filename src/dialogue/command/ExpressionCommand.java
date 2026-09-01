package dialogue.command;

import dialogue.DialogueCommand;
import dialogue.DialogueDebug;
import dialogue.DialogueRunner;

public class ExpressionCommand implements DialogueCommand {
	private final String expression;

	public ExpressionCommand(String expression) {
		this.expression = expression;
	}

	@Override
	public void execute(DialogueRunner runner) {
		runner.getCurrentSpeaker().setExpression(expression);
		DialogueDebug.log(this, "Expression: " + expression);
	}
}