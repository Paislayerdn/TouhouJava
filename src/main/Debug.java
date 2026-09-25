package main;

public final class Debug {
	private Debug() {}

	public static void log(String namespace, Class<?> source, String message) {
		log(namespace + " " + source.getSimpleName(), message);
	}
	public static void log(String namespace, Object source, String message) {
		log(namespace + " " + source.getClass().getSimpleName(), message);
	}
	public static void log(Class<?> source, String message) {
		log(source.getPackageName() + " " + source.getSimpleName(), message);
	}
	public static void log(Object source, String message) {
		Class<?> type = source.getClass();
		log(type.getPackageName() + " " + type.getSimpleName(), message);
	}
	public static void log(String message) {
		log("Anonymous", message);
	}
	public static void log(String type, String message) {
		print("[" + type + "] " + message);
	}

	public static void warn(String namespace, Class<?> source, String message) {
		warn(namespace + " " + source.getSimpleName(), message);
	}
	public static void warn(String namespace, Object source, String message) {
		warn(namespace + " " + source.getClass().getSimpleName(), message);
	}
	public static void warn(Class<?> source, String message) {
		warn(source.getPackageName() + " " + source.getSimpleName(), message);
	}
	public static void warn(Object source, String message) {
		Class<?> type = source.getClass();
		warn(type.getPackageName() + " " + type.getSimpleName(), message);
	}
	public static void warn(String message) {
		warn("Anonymous", message);
	}
	public static void warn(String type, String message) {
		err("[" + type + "] Warning: " + message);
	}

	public static IllegalStateException terminate(String namespace, Class<?> source, String message) {
		return terminate(namespace + " " + source.getSimpleName(), message);
	}
	public static IllegalStateException terminate(String namespace, Object source, String message) {
		return terminate(namespace + " " + source.getClass().getSimpleName(), message);
	}
	public static IllegalStateException terminate(Class<?> source, String message) {
		return terminate(
			source.getPackageName() + " " + source.getSimpleName(),
			message
		);
	}
	public static IllegalStateException terminate(Object source, String message) {
		Class<?> type = source.getClass();
		return terminate(type.getPackageName() + " " + type.getSimpleName(), message);
	}
	public static IllegalStateException terminate(String type, String message) {
		return terminate("[" + type + "] " + message);
	}

	public static IllegalStateException terminate(String message) {
		return new IllegalStateException(message);
	}

	private static void print(String message) {
		System.out.println(message);
	}
	private static void err(String message) {
		System.err.println(message);
	}
}