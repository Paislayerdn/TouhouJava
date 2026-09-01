package dialogue.command;

import dialogue.DialogueCommand;
import dialogue.DialogueDebug;
import dialogue.DialogueRunner;

public class MiscCommand implements DialogueCommand {
	private final String command;
	private final String argument;

	public MiscCommand(String command, String argument) {
		this.command = command;
		this.argument = argument;
	}

	@Override
	public void execute(DialogueRunner runner) {
		switch (command) {
			case "print" -> DialogueDebug.log(argument);
			case "kill" -> runner.removeCurrentSpeaker();
			case "name" -> runner.setCurrentSpeakerName(argument);
			default -> DialogueDebug.log(this, "Unknown misc command: " + command);
		}
	}

	public String getCommand() { return command; }
	public String getArgument() { return argument; }
}