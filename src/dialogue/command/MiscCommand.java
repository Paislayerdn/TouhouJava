package dialogue.command;

import main.Debug;
import dialogue.DialogueCommand;
import dialogue.DialogueRunner;

public final class MiscCommand implements DialogueCommand {
	private final String command;
	private final String argument;

	public MiscCommand(String command, String argument) {
		this.command = command;
		this.argument = argument;
	}

	@Override
	public void execute(DialogueRunner runner) {
		switch (command) {
			case "print" -> Debug.log(argument);
			case "kill" -> runner.removeCurrentSpeaker();
			case "name" -> runner.setCurrentSpeakerName(argument);
			default -> Debug.log(this, "Unknown misc command: " + command);
		}
	}

	public String getCommand() { return command; }
	public String getArgument() { return argument; }
}