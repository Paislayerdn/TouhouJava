// FACADE
package action;

import entity.Entity;

public final class JScratch {
	public static WaitAction Wait(Object x) { return new WaitAction(x); }
	
	public static Action MoveX(Object x) { return Move(x, 0); }
	public static Action MoveY(Object y) { return Move(0, y); }
	public static Action Move(Object x, Object y) { return new MoveAction(x, y); }
	public static Action Warp(Entity target) { return GoTo(target); }
	public static Action GoTo(Entity target) { return new GoToAction(target); }
	public static Action Warp(Object x, Object y) { return GoTo(x, y); }
	public static Action GoTo(Object x, Object y) { return new SetAction(x, y); }
	public static Action SetX(Object x) { return new SetAction(x, SetAction.Axis.X); }
	public static Action SetY(Object y) { return new SetAction(y, SetAction.Axis.Y); }
	public static Action Forward(Object distance) { return new ForwardAction(distance); }

	public static Action Turn(Object angle) { return new TurnAction(angle); }
	public static Action Look(Object angle) { return new LookAction(angle); }
	public static Action LookTowards(Entity target) { return new LookTowardsAction(target); }

	public static Action Sound(String name, String path) {
		return new SoundAction(name, path);
	}
	public static SoundValue GetSound(String name) { return new SoundValue(name); }
	public static Action SetSoundVolume(String name, float volume) { return GetSound(name).setVolume(volume); }
	public static Action PlaySound(String name) { return GetSound(name).play(); }
		public static VariableAction Declare(String name, Object value) { return Var(name, value); }
	public static VariableAction Var(String name, Object value) {
		return new VariableAction( name, VariableAction.Operation.DECLARE, value);
	}
	public static Sequence Vars(Object... values) {
		if (values.length % 2 != 0) {
			throw new IllegalArgumentException(
				"Vars requires name/value pairs."
			);
		}

		Action[] actions = new Action[values.length / 2];

		for (int i = 0; i < values.length; i += 2) {
			String name = (String) values[i];
			Object value = values[i + 1];

			actions[i / 2] = Var(name, value);
		}

		return new Sequence(actions);
	}
	public static VariableAction Set(String name, Object value) {
		return new VariableAction(name, VariableAction.Operation.SET, value);
	}
	public static VariableAction Change(String name, Object value) {
		return new VariableAction(name, VariableAction.Operation.CHANGE,value);
	}
	public static Value Get(String name) { return action -> action.getVariable(name); }
	
	public static SpawnBulletAction SpawnBullet(Object index, Action action) {
		return new SpawnBulletAction(index, action);
	}
	public static SpawnBulletAction SpawnBullet(Object index, double x, double y, double angle, Action action) {
		return new SpawnBulletAction(index, x, y, angle, action);
	}
	public static DestroyAction Destroy() { return new DestroyAction(); }
	
	public static Sequence Seq(Action... actions) { return Sequence(actions); }
	public static Sequence Sequence(Action... actions) { return new Sequence(actions); }

	public static Parallel Paralell(Action... actions) {
		JDebug.log("Warning, you're mispelling \"Parallel\"...");
		return Parallel(actions);
	}
	public static Parallel Par(Action... actions) { return Parallel(actions); }
	public static Parallel Parallel(Action... actions) { return new Parallel(actions); }

	public static Forever Forever(Object type, Action... actions) {
		Action container = null;
		
		if (type instanceof Number) {
			int value = ((Number) type).intValue();

			if (value == 0) {
				container = new Sequence(actions);
			} else {
				container = new Parallel(actions);
			}

		} else if (type instanceof String) {
			String mode = ((String) type).toLowerCase().trim();

			if (mode.startsWith("s")) {
				container = new Sequence(actions);
			} else if (mode.startsWith("p")) {
				container = new Parallel(actions);
			}
		}

		if (container == null) {
			JDebug.log(String.format("[JScratch] Warning, unknown Forever mode: \"%s\"", type));
			JDebug.log("Try be sober. Defaulting to Sequence.");

			container = new Sequence(actions);
		}
		return new Forever(container);
	}
	
	public static ForAction For(String variable, Object start, Object end, ActionFactory factory) {
		return new ForAction(variable, start, end, factory);
	}
	
	public static Value Add(Object... values) { return MathValue.Add(values); }
	public static Value Sub(Object... values) { return MathValue.Sub(values); }
	public static Value Mul(Object... values) { return MathValue.Mul(values); }
	public static Value Div(Object... values) { return MathValue.Div(values); }
	
	public static Value Min(Object... values) { return MathValue.Min(values); }
	public static Value Max(Object... values) { return MathValue.Max(values); }
	
	public static Value Random() { return MathValue.Random(); }
	public static Value Random(Object a, Object b) { return MathValue.Random(a, b); }
	
	
	
	public static PrintAction Print(Object message) { return new PrintAction(message); }
}