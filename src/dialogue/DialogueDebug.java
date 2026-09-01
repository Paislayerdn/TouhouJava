package dialogue;

public final class DialogueDebug {
	private DialogueDebug() {}

	public static void log(Class<?> source, String message) {
		System.out.println(
			"[Dialogue " + source.getSimpleName() + "] " + message
		);
	}

	public static void log(Object source, String message) {
		System.out.println(
			"[Dialogue " + source.getClass().getSimpleName() + "] " + message
		);
	}

	public static void log(String message) {
		System.out.println("[Dialogue] " + message);
	}
}