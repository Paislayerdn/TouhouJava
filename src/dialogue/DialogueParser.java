package dialogue;

import static dialogue.DialogueCommand.*;
import dialogue.command.*;

import java.util.Deque;
import java.util.Locale;

public final class DialogueParser {
	private DialogueParser() {}

	public static Dialogue parse(String source) {
		Dialogue dialogue = new Dialogue();

		Deque<String> tokens = DialogueLexer.tokenize(source);

		while (!tokens.isEmpty()) {
			String token = tokens.poll();

			if (token.isEmpty())
				continue;

			switch (token.toLowerCase(Locale.ROOT)) {
				case SPEAKER ->
					dialogue.addCommand( new SpeakerCommand(tokens.poll()) );

				case EXPRESSION ->
					dialogue.addCommand( new ExpressionCommand(tokens.poll()) );

				case SFX ->
					dialogue.addCommand( new SFXCommand(tokens.poll()) );

				case TEXT ->
					dialogue.addCommand( new TextCommand( tokens.poll()) );

				case MISC ->
					dialogue.addCommand( parseMisc(tokens) );

				// default -> Debug.log("Unknown token: " + token);
			}
		}

		return dialogue;
	}

	private static DialogueCommand parseMisc(Deque<String> tokens) {
		String command = tokens.poll();
		String argument = "";

		String normalized = command.toLowerCase(Locale.ROOT);

		if (normalized.equals("name") || normalized.equals("print"))
			argument = tokens.poll();

		if (normalized.equals("print") || normalized.equals("name") || normalized.equals("kill")) {
			return new MiscCommand(normalized, argument);
		}

		return new JScratchCommand(command);
	}
}
