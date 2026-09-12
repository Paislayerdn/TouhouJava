package dialogue;

import static dialogue.DialogueCommand.*;
import dialogue.command.*;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;

public final class DialogueParser {
	private DialogueParser() {}

	public static Dialogue parse(String source) {
		source = source.replace("\r\n", "/n");
		source = source.replace("\n", "/n");
		source = source.replace("\r", "/n");

		Dialogue dialogue = new Dialogue();

		Deque<String> tokens = new ArrayDeque<>(
			Arrays.asList(source.split("/n", -1))
		);

		while (!tokens.isEmpty()) {
			String token = tokens.poll();

			if (token.isEmpty())
				continue;

			switch (token) {
				case SPEAKER:		dialogue.addCommand( new SpeakerCommand(tokens.poll()) ); break;
				case EXPRESSION:	dialogue.addCommand( new ExpressionCommand(tokens.poll()) ); break;
				case MOVE:			dialogue.addCommand( parseMove(tokens) ); break;
				case SFX:			dialogue.addCommand( new SFXCommand(tokens.poll()) ); break;
				case TEXT:			dialogue.addCommand( new TextCommand( tokens.poll()) ); break;
				case MISC:			dialogue.addCommand( parseMisc(tokens) );  break;
				default:			DialogueDebug.log("Unknown token: " + token); break;
			}
		}

		return dialogue;
	}

	private static MoveCommand parseMove(Deque<String> tokens) {
		String[] args = tokens.poll().split(",");

		double x = Double.parseDouble(args[0]);
		double y = Double.parseDouble(args[1]);
		int frames = Integer.parseInt(args[2]);
		boolean blocking = !args[3].equals("0");

		return new MoveCommand(x, y, frames, blocking);
	}

	private static MiscCommand parseMisc(Deque<String> tokens) {
		String command = tokens.poll();
		String argument = "";

		if (command.equals("name") || command.equals("print"))
			argument = tokens.poll();

		return new MiscCommand(command, argument);
	}
}