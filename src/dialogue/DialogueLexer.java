package dialogue;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;

public final class DialogueLexer {
	private DialogueLexer() {}

	public static Deque<String> tokenize(String source) {
		source = source.replace("\r\n", "/n");
		source = source.replace("\n", "/n");
		source = source.replace("\r", "/n");

		return new ArrayDeque<>(
			Arrays.asList(source.split("/n", -1))
		);
	}

	public static String substitute(DialogueRunner runner, String text) {
		StringBuilder result = new StringBuilder();

		for (int i = 0; i < text.length(); i++) {
			char c = text.charAt(i);

			if (c != '%') {
				result.append(c);
				continue;
			}

			// %%
			if (i + 1 < text.length()
					&& text.charAt(i + 1) == '%') {
				result.append('%');
				i++;
				continue;
			}

			int end = text.indexOf('%', i + 1);

			// Unclosed %
			if (end == -1) {
				result.append('%');
				continue;
			}

			String name = text.substring(i + 1, end);
			Object value = runner.resolveVariable(name);

			if (value == null) {
				throw new IllegalArgumentException(
					"[DialogueLexer] Unknown dialogue variable: " + name
				);
			}

			result.append(value);
			i = end;
		}

		return result.toString();
	}
}