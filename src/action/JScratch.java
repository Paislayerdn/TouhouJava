// FACADE
package action;

import java.util.ArrayList;
import java.util.Arrays;
import java.awt.image.BufferedImage;

import static action.AngleAction.*;

import entity.Thing;

public final class JScratch {
	
	// POSITION
	public static Action Forward(Object distance) { return new ForwardAction(distance); }
	public static Action MoveX(Object x) { return Move(x, 0); }
	public static Action MoveY(Object y) { return Move(0, y); }
	public static Action Move(Object x, Object y) { return new MoveAction(x, y); }
	
	public static Action GoTo(Thing target) { return new GoToAction(target); }
	public static Action Warp(Thing target) { return GoTo(target); }
	public static Action GoTo(Object x, Object y) { return new SetAction(x, y); }
	public static Action Warp(Object x, Object y) { return GoTo(x, y); }
	public static Action SetX(Object x) { return new SetAction(x, SetAction.Axis.X); }
	public static Action SetY(Object y) { return new SetAction(y, SetAction.Axis.Y); }
	
	// TWEEN: POSITION
	public static Action GoTo(Thing target, int frames) {
		return Parallel(
			TweenX((Value) action -> target.getX(), frames),
			TweenY((Value) action -> target.getY(), frames)
		);
	}
	public static Action Warp(Thing target, int frames) { return GoTo(target, frames); }
	public static Action GoTo(Object x, Object y, int frames) {
		return Parallel(
			TweenX(x, frames),
			TweenY(y, frames)
		);
	}
	public static Action GoTo(Object startX, Object startY, Object x, Object y, int frames) {
		return Parallel(
			TweenX(startX, x, frames),
			TweenY(startY, y, frames)
		);
	}
	public static Action SetX(Object x, Object frames) { return TweenX(x, frames); }
	public static Action SetX(Object start, Object end, int frames) { return TweenX(start, end, frames); }
	public static Action SetY(Object y, Object frames) { return TweenY(y, frames); }
	public static Action SetY(Object start, Object end, int frames) { return TweenY(start, end, frames); }
	

	// ANGLE, THE FIRST 3 ALWAYS FOLLOW ANGLEOVERRIDE
	public static Action LookTowards(Thing target) { return new LookTowardsAction(target); }
	public static Action Look(Object angle) { return new AngleAction(Angle.ACTIVE, Operation.SET, angle); }
	public static Action Turn(Object angle) { return new AngleAction(Angle.ACTIVE, Operation.CHANGE, angle); }
	// TWEEN: ANGLE
	public static Action LookTowards(Thing target, int frames) {
		return new TweenAction(
			ReservedVariable.ANGLE.getName(),
			Value.Get("angle"),
			(Value) action -> {
				Thing owner = action.getOwner();

				float dx = target.getX() - owner.getX();
				float dy = target.getY() - owner.getY();

				return (float) Math.toDegrees(Math.atan2(dy, dx));
			},
			frames, Easing.LINEAR, TweenMode.CHASING
		);
	}
	public static Action Look(Object angle, int frames) {
		return new TweenAction(ReservedVariable.ANGLE.getName(),
			angle, frames, Easing.LINEAR, TweenMode.SNAPSHOT
		);
	}
	public static Action Look(Object start, Object end, int frames) {
		return new TweenAction(ReservedVariable.ANGLE.getName(),
			start, end, frames,
			Easing.LINEAR, TweenMode.SNAPSHOT
		);
	}
	public static Action Turn(Object angle, int frames) {
		return new TweenAction(
			ReservedVariable.ANGLE.getName(),
			(Value) action -> Value.Get("angle"),
			(Value) action -> Add(Value.Get("angle"), angle),
			frames, Easing.LINEAR, TweenMode.SNAPSHOT
		);
	}
	
	public static Action SetTrueAngle(Object angle) { return new AngleAction(Angle.TRUE, Operation.SET, angle); }
	public static Action ChangeTrueAngle(Object angle) { return new AngleAction(Angle.TRUE, Operation.CHANGE, angle); }
	public static Action SetAppearAngle(Object angle) { return new AngleAction(Angle.APPEAR, Operation.SET, angle); }
	public static Action ChangeAppearAngle(Object angle) { return new AngleAction(Angle.APPEAR, Operation.CHANGE, angle); }
	public static Action EnableAngleOverride() { return new AngleOverrideAction(true); }
	public static Action DisableAngleOverride() { return new AngleOverrideAction(false); }
	
	// APPEARANCE
	public static Action SetCostume(String name) { return new SetCostume(name); }
	public static Action SetCostume(BufferedImage image) { return new SetCostume(image); }
	
	public static Action SetColor(Object color) { return new SetColor(color); }
	public static Action ChangeColor(Object amount) { return new ChangeColor(amount); }

	public static Action SetPixelate(Object pixelate) { return new SetPixelate(pixelate); }
	public static Action ChangePixelate(Object amount) { return new ChangePixelate(amount); }

	public static Action SetBrightness(Object brightness) { return new SetBrightness(brightness); }
	public static Action ChangeBrightness(Object amount) { return new ChangeBrightness(amount); }

	public static Action SetGhost(Object ghost) { return new SetGhost(ghost); }
	public static Action ChangeGhost(Object amount) { return new ChangeGhost(amount); }

	public static Action SetSize(Object size) { return new SetSize(size); }
	public static Action ChangeSize(Object amount) { return new ChangeSize(amount); }
	
	// HITBOX, ONLY FOR ENTITY
	public static Action AddCircleHitbox(String name, Object radius) { return new AddCircleHitbox(name, radius); }
	public static Action AddRectangleHitbox(String name, Object width, Object height) { return new AddRectangleHitbox(name, width, height); }

	public static Action EnableHitbox(String name) { return new SetHitboxEnabled(name, true); }
	public static Action DisableHitbox(String name) { return new SetHitboxEnabled(name, false); }

	public static Action AddHitboxTag(String name, String tag) { return new SetHitboxTag(name, tag, true); }
	public static Action RemoveHitboxTag(String name, String tag) { return new SetHitboxTag(name, tag, false); }
	
	// SOUND
	public static Action Sound(String name, String path) { return new SoundAction(name, path); }
	public static Action SetSoundVolume(String name, Object volume) { return new SetSoundVolumeAction(name, volume); }
	public static Action PlaySound(String name) { return new PlaySoundAction(name); }
	
	// VARIABLE
	public static VariableAction Declare(String name, Object value) { return Var(name, value); }
	public static VariableAction Var(String name, Object value) { return new VariableAction( name, VariableAction.Operation.DECLARE, value); }
	public static VariableAction Set(String name, Object value) { return new VariableAction(name, VariableAction.Operation.SET, value); }
	public static VariableAction Change(String name, Object value) { return new VariableAction(name, VariableAction.Operation.CHANGE,value); }
	public static Value Get(String name) { return Value.Get(name); }
	
	// BULLET LIFE
	public static Action SpawnBullet(Action action) { return new SpawnBulletAction(action); }
	public static Action Destroy() { return new DestroyAction(); }
	
	// LOGIC
	public static Value And(Object... values) { return LogicValue.And(values); }
	public static Value Or(Object... values) { return LogicValue.Or(values); }
	public static Value Not(Object value) { return LogicValue.Not(value); }
	// COMPARISON
	public static Value Greater(Object... values) { return CompareValue.Greater(values); }
	public static Value GreaterEqual(Object... values) { return CompareValue.GreaterEqual(values); }
	public static Value Less(Object... values) { return CompareValue.Less(values); }
	public static Value LessEqual(Object... values) { return CompareValue.LessEqual(values); }
	public static Value Equal(Object... values) { return CompareValue.Equal(values); }

	// PROCESS CONTROL
	public static Action Seq(Action... actions) { return Sequence(actions); }
	public static Action Sequence(Action... actions) { return new Sequence(actions); }

	public static Action Paralell(Action... actions) {
		JSCDebug.log("Warning, you're mispelling \"Parallel\"...");
		return Parallel(actions);
	}
	public static Action Par(Action... actions) { return Parallel(actions); }
	public static Action Parallel(Action... actions) { return new Parallel(actions); }
	
	// CONTROL FLOW
	public static Action Wait(Object x) { return new WaitAction(x); }
	public static Action Wait() { return new WaitAction(); }
	public static Action WaitUntil(Object condition) { return new WaitUntilAction(condition); }
	
	public static Action If(Object condition, ActionFactory thenFactory) { return new IfAction(condition, thenFactory); }
	public static Action If(Object condition, ActionFactory thenFactory, ActionFactory elseFactory) { return new IfAction(condition, thenFactory, elseFactory); }

	public static Action For(String variable, Object start, Object end, ActionFactory factory) { return new ForAction(variable, start, end, factory); }
	public static Action While(Object condition, ActionFactory factory) { return new WhileAction(condition, factory); }
	public static Action RepeatUntil(Object condition, ActionFactory factory) { return new RepeatUntilAction(condition, factory); }	
	public static Action Forever(Object type, Action... actions) {
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
			JSCDebug.log(String.format("[JScratch] Warning, unknown Forever mode: \"%s\"", type));
			JSCDebug.log("Try be sober. Defaulting to Sequence.");

			container = new Sequence(actions);
		}
		return new Forever(container);
	}
	
	
	// MATH
	public static Value Add(Object... values) { return MathValue.Add(values); }
	public static Value Sub(Object... values) { return MathValue.Sub(values); }
	public static Value Mul(Object... values) { return MathValue.Mul(values); }
	public static Value Div(Object... values) { return MathValue.Div(values); }
	public static Value Mod(Object... values) { return MathValue.Mod(values); }
	public static Value Pow(Object a, Object b) { return Power(a, b); }
	public static Value Power(Object a, Object b) { return MathValue.Power(a, b); }
	public static Value Root(Object a, Object b) { return MathValue.Root(a, b); }
	
	public static Value Abs(Object value) { return MathValue.Abs(value); }
	public static Value Min(Object... values) { return MathValue.Min(values); }
	public static Value Max(Object... values) { return MathValue.Max(values); }
	
	public static Value Random() { return MathValue.Random(); }
	public static Value Random(Object a, Object b) { return MathValue.Random(a, b); }
	public static Value RandomSign() { return MathValue.RandomSign(); }
	
	// TWEENSERVICE, SYNTAX SUGAR TWEEN
	public static Action Tween(Object... args) {
		Easing easing = Easing.LINEAR;

		if (args[args.length - 1] instanceof Easing) {
			easing = (Easing) args[args.length - 1];
			args = Arrays.copyOf(args, args.length - 1);
		}

		Object frames = args[args.length - 1];
		args = Arrays.copyOf(args, args.length - 1);

		ArrayList<Action> tweens = new ArrayList<>();

		int i = 0;

		while (i < args.length) {
			String property = (String) args[i++];

			Object startOrEnd = args[i++];

			if (i < args.length && !(args[i] instanceof String)) {
				Object end = args[i++];

				tweens.add(
					new TweenAction(property, startOrEnd, end, frames, easing)
				);
			} else {
				tweens.add(
					new TweenAction(property, startOrEnd, frames, easing)
				);
			}
		}

		return Parallel(tweens.toArray(new Action[0]));
	}
	
	// TWEENSERVICE: POSITION HELPER
	private static Action TweenX(Object end, Object frames) {
		return new TweenAction(ReservedVariable.X.getName(),
			Value.Get("x"), end, frames,
			Easing.LINEAR
		);
	}
	private static Action TweenX(Object start, Object end, Object frames) {
		return new TweenAction(ReservedVariable.X.getName(),
			start, end, frames,
			Easing.LINEAR
		);
	}
	private static Action TweenY(Object end, Object frames) {
		return new TweenAction(ReservedVariable.Y.getName(),
			Value.Get("y"), end, frames,
			Easing.LINEAR
		);
	}
	private static Action TweenY(Object start, Object end, Object frames) {
		return new TweenAction(ReservedVariable.Y.getName(),
			start, end, frames,
			Easing.LINEAR
		);
	}
	
	
	// PRINT
	public static Action Print(Object message) { return new PrintAction(message); }
	public static Action LuaPrint(Object message) { return new JSLPrintAction(message); }
}

final class JSCDebug {
	private JSCDebug() {}

	public static void log(Class<?> source, String message) {
		print("[JScratch " + source.getSimpleName() + "] " + message);
	}
	public static void log(Object source, String message) {
		print("[JScratch " + source.getClass().getSimpleName() + "] " + message);
	}
	public static void log(String message) {
		print("[JScratch] " + message);
	}
	private static void print(String message) {
		System.out.println(message);
	}
	
	public static RuntimeException error(Class<?> source, String message) {
		return fail("[JScratch " + source.getSimpleName() + "] " + message);
	}
	public static RuntimeException error(Object source, String message) {
		return fail("[JScratch " + source.getClass().getSimpleName() + "] " + message);
	}
	public static RuntimeException error(String type, String message) {
		return fail("[JScratch " + type + "] " + message);
	}
	public static RuntimeException error(String message) {
		return fail("[JScratch] " + message);
	}
	private static RuntimeException fail(String message) {
		throw new IllegalStateException(message);
	}
}
