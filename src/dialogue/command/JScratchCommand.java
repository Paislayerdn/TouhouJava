package dialogue.command;

import action.Action;
import action.JScratch;
import static action.JScratch.*;
import dialogue.DialogueCommand;
import dialogue.DialogueRunner;
import dialogue.DialogueThing;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import main.Debug;

/** Runs a JScratch action on the current dialogue speaker. */
public final class JScratchCommand implements DialogueCommand {
	private final String expression;

	public JScratchCommand(String expression) {
		this.expression = expression.trim();
	}

	@Override
	public void execute(DialogueRunner runner) {
		DialogueThing speaker = runner.getCurrentSpeaker();
		if (speaker == null) {
			throw Debug.terminate(this, "JScratch command requires a current speaker: " + expression);
		}

		Action action = invokeAction(expression, speaker);
		// Each command is submitted independently, so the speaker's ActionRunner
		// advances it alongside its other actions without blocking dialogue.
		speaker.run(action);
	}

	private static Action invokeAction(String source, DialogueThing speaker) {
		Call call = Call.parse(source);
		String actionName = canonicalActionName(call.name);
		List<String> arguments = splitArguments(call.arguments);
		if (actionName.equals("Tween") && !arguments.isEmpty()) {
			arguments.set(0, canonicalScriptProperty(speaker, arguments.get(0)));
		}
		Action direct = invokeCommonFacadeActions(actionName, arguments);
		if (direct != null) return direct;

		Method[] methods = JScratch.class.getMethods();

		for (Method method : methods) {
			if (!method.getName().equals(actionName)
					|| !Modifier.isStatic(method.getModifiers())
					|| !Action.class.isAssignableFrom(method.getReturnType())) {
				continue;
			}

			Object[] converted = convertArguments(method, arguments);
			if (converted == null) continue;

			try {
				return (Action) method.invoke(null, converted);
			} catch (IllegalAccessException | InvocationTargetException e) {
				Throwable cause = e instanceof InvocationTargetException invocation
					? invocation.getCause() : e;
				throw Debug.terminate(JScratchCommand.class,
					"Could not execute JScratch command '" + source + "': " + cause);
			}
		}

		throw Debug.terminate(JScratchCommand.class,
			"Unknown or invalid JScratch action: " + source);
	}

	private static String canonicalActionName(String name) {
		for (Method method : JScratch.class.getMethods()) {
			if (Modifier.isStatic(method.getModifiers())
					&& Action.class.isAssignableFrom(method.getReturnType())
					&& method.getName().equalsIgnoreCase(name)) {
				return method.getName();
			}
		}
		return name;
	}

	private static String canonicalScriptProperty(DialogueThing speaker, String source) {
		String property = unquote(source);
		String canonical = speaker.findScriptProperty(property, true);
		if (canonical != null) return canonical;
		return source;
	}

	/* Common dialogue-sprite actions use the facade directly. Reflection below
	 * remains as the generic fallback so other JScratch actions stay available. */
	private static Action invokeCommonFacadeActions(String name, List<String> args) {
		Object[] values = new Object[args.size()];
		for (int i = 0; i < args.size(); i++) values[i] = parseLiteral(args.get(i));

		return switch (name) {
			case "SetColor" -> args.size() == 1 ? SetColor(values[0]) : null;
			case "ChangeColor" -> args.size() == 1 ? ChangeColor(values[0]) : null;
			case "SetDesaturation" -> args.size() == 1 ? SetDesaturation(values[0]) : null;
			case "ChangeDesaturation" -> args.size() == 1 ? ChangeDesaturation(values[0]) : null;
			case "SetPixelate" -> args.size() == 1 ? SetPixelate(values[0]) : null;
			case "ChangePixelate" -> args.size() == 1 ? ChangePixelate(values[0]) : null;
			case "SetBrightness" -> args.size() == 1 ? SetBrightness(values[0]) : null;
			case "ChangeBrightness" -> args.size() == 1 ? ChangeBrightness(values[0]) : null;
			case "SetGhost" -> args.size() == 1 ? SetGhost(values[0]) : null;
			case "ChangeGhost" -> args.size() == 1 ? ChangeGhost(values[0]) : null;
			case "SetSize" -> args.size() == 1 ? SetSize(values[0]) : null;
			case "ChangeSize" -> args.size() == 1 ? ChangeSize(values[0]) : null;
			case "MoveX" -> args.size() == 1 ? MoveX(values[0]) : null;
			case "MoveY" -> args.size() == 1 ? MoveY(values[0]) : null;
			case "Move" -> args.size() == 2 ? Move(values[0], values[1]) : null;
			case "SetX" -> args.size() == 1 ? SetX(values[0]) : null;
			case "SetY" -> args.size() == 1 ? SetY(values[0]) : null;
			case "SetCostume" -> args.size() == 1
				? SetCostume(String.valueOf(values[0])) : null;
			case "Tween" -> args.size() >= 2 ? Tween(values) : null;
			case "Destroy" -> args.isEmpty() ? Destroy() : null;
			default -> null;
		};
	}

	private static Object[] convertArguments(Method method, List<String> arguments) {
		Class<?>[] parameterTypes = method.getParameterTypes();
		boolean varargs = method.isVarArgs();
		if ((!varargs && parameterTypes.length != arguments.size())
				|| (varargs && arguments.size() < parameterTypes.length - 1)) {
			return null;
		}

		Object[] result = new Object[parameterTypes.length];
		for (int i = 0; i < parameterTypes.length; i++) {
			if (varargs && i == parameterTypes.length - 1) {
				Class<?> component = parameterTypes[i].getComponentType();
				Object values = java.lang.reflect.Array.newInstance(
					component, arguments.size() - i);
				for (int j = i; j < arguments.size(); j++) {
					Object value = convert(arguments.get(j), component);
					if (value == Conversion.FAIL) return null;
					java.lang.reflect.Array.set(values, j - i, value);
				}
				result[i] = values;
				break;
			}

			Object value = convert(arguments.get(i), parameterTypes[i]);
			if (value == Conversion.FAIL) return null;
			result[i] = value;
		}
		return result;
	}

	private static Object convert(String source, Class<?> target) {
		String token = source.trim();
		if (target == String.class) return unquote(token);

		Object literal = parseLiteral(token);
		if (target == Object.class) return literal;
		if (target == int.class || target == Integer.class) {
			return literal instanceof Number n ? n.intValue() : Conversion.FAIL;
		}
		if (target == long.class || target == Long.class) {
			return literal instanceof Number n ? n.longValue() : Conversion.FAIL;
		}
		if (target == float.class || target == Float.class) {
			return literal instanceof Number n ? n.floatValue() : Conversion.FAIL;
		}
		if (target == double.class || target == Double.class) {
			return literal instanceof Number n ? n.doubleValue() : Conversion.FAIL;
		}
		if (target == boolean.class || target == Boolean.class) {
			return literal instanceof Boolean ? literal : Conversion.FAIL;
		}
		if (target.isEnum() && literal instanceof String name) {
			try {
				@SuppressWarnings({"rawtypes", "unchecked"})
				Object value = Enum.valueOf((Class<? extends Enum>) target, name);
				return value;
			} catch (IllegalArgumentException e) {
				return Conversion.FAIL;
			}
		}
		return target.isInstance(literal) ? literal : Conversion.FAIL;
	}

	private static Object parseLiteral(String source) {
		String token = source.trim();
		if (token.length() >= 2
				&& ((token.charAt(0) == '"' && token.charAt(token.length() - 1) == '"')
				|| (token.charAt(0) == '\'' && token.charAt(token.length() - 1) == '\''))) {
			return unquote(token);
		}
		if (token.equalsIgnoreCase("true")) return true;
		if (token.equalsIgnoreCase("false")) return false;
		try {
			if (token.matches("[-+]?\\d+")) return Integer.parseInt(token);
			return Double.parseDouble(token);
		} catch (NumberFormatException ignored) {
			return token;
		}
	}

	private static String unquote(String source) {
		String token = source.trim();
		if (token.length() >= 2
				&& ((token.charAt(0) == '"' && token.charAt(token.length() - 1) == '"')
				|| (token.charAt(0) == '\'' && token.charAt(token.length() - 1) == '\''))) {
			return token.substring(1, token.length() - 1);
		}
		return token;
	}

	private static List<String> splitArguments(String source) {
		ArrayList<String> result = new ArrayList<>();
		if (source.isBlank()) return result;

		int start = 0;
		char quote = 0;
		int depth = 0;
		for (int i = 0; i < source.length(); i++) {
			char c = source.charAt(i);
			if (quote != 0) {
				if (c == quote && (i == 0 || source.charAt(i - 1) != '\\')) quote = 0;
				continue;
			}
			if (c == '"' || c == '\'') quote = c;
			else if (c == '(' || c == '[') depth++;
			else if (c == ')' || c == ']') depth--;
			else if (c == ',' && depth == 0) {
				result.add(source.substring(start, i).trim());
				start = i + 1;
			}
		}
		result.add(source.substring(start).trim());
		return result;
	}

	private enum Conversion { FAIL }

	private record Call(String name, String arguments) {
		static Call parse(String source) {
			int open = source.indexOf('(');
			char close = ')';
			if (open < 0 || !source.endsWith(")")) {
				open = source.indexOf('[');
				close = ']';
			}
			if (open < 1 || source.charAt(source.length() - 1) != close) {
				throw Debug.terminate(JScratchCommand.class,
					"Expected JScratch call like SetColor(15) or SetColor[15]: " + source);
			}
			return new Call(source.substring(0, open).trim(),
				source.substring(open + 1, source.length() - 1));
		}
	}
}
