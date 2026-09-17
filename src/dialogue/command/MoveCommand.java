package dialogue.command;

import dialogue.DialogueCommand;
import dialogue.DialogueDebug;
import dialogue.DialogueRunner;
import dialogue.DialogueSpeaker;

public class MoveCommand implements DialogueCommand {
	private final float x;
	private final float y;
	private final int duration;
	private final boolean blocking;

	public MoveCommand(
		float x,
		float y,
		int duration,
		boolean blocking
	) {
		this.x = x;
		this.y = y;
		this.duration = duration;
		this.blocking = blocking;
	}

	@Override
	public void execute(DialogueRunner runner) {
		DialogueSpeaker speaker = runner.getCurrentSpeaker();

		runner.startMovement(speaker, x, y, duration, blocking);
		DialogueDebug.log(this,
			"Move to (" + x + ", " + y
			+ ") over " + duration
			+ " frames"
			+ (blocking ? " [blocking]" : "")
		);
	}
}