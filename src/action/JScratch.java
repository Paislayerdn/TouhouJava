package action;

public class JScratch {
	public static Sequence Sequence(Action... actions) {
		return new Sequence(actions);
	}
	
	public static Sequence Seq(Action... actions) {
		return new Sequence(actions);
	}

	public static Parallel Parallel(Action... actions) {
		System.out.println("[JScratch Warning] You're mispelling Parallel...");
		return new Parallel(actions);
	}
	
	public static Parallel Par(Action... actions) {
		return new Parallel(actions);
	}

	public static Parallel Paralell(Action... actions) {
		return new Parallel(actions);
	}

	public static Forever Forever(Object type, Action... actions) {
		Action container = null;
		
		if(type instanceof Number) {
			int value = ((Number) type).intValue();

			if(value == 0) {
				container = new Sequence(actions);
			}
			else if(value == 1) {
				container = new Parallel(actions);
			}

		} else if(type instanceof String) {
			String mode = ((String) type)
					.toLowerCase()
					.trim();

			if(mode.startsWith("s")) {
				container = new Sequence(actions);
			}
			else if(mode.startsWith("p")) {
				container = new Parallel(actions);
			}
		}


		if(container == null) {
			System.out.println(
				"[JScratch Warning] Unknown Forever mode: \""
				+ type
				+ "\""
			);

			System.out.println(
				"Try be sober. Defaulting to Sequence."
			);

			container = new Sequence(actions);
		}
		return new Forever(container);
	}
}