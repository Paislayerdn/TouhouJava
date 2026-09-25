package dialogue.command;

import main.Debug;

import action.Action;
import static action.JScratch.*;
import dialogue.*;

public final class MoveCommand implements DialogueCommand {
	private final float x;
	private final float y;
	private final int duration;
	private final boolean blocking;

	public MoveCommand(
		float x, float y,
		int duration, boolean blocking
	) {
		this.x = x;
		this.y = y;
		this.duration = duration;
		this.blocking = blocking;
	}

	@Override
	public void execute(DialogueRunner runner) {
		DialogueThing speaker = runner.getCurrentSpeaker();

		Action movement = GoTo(x, y, duration);

		speaker.run(movement);

		if (blocking) runner.waitForMovement(movement);

		Debug.log(this,
			"Move to (" + x + ", " + y
			+ ") over " + duration
			+ " frames"
			+ (blocking ? " [blocking]" : "")
		);
	}
}