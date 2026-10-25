// FACADE
package action;

import java.util.Locale;
import java.util.ArrayList;
import java.util.Arrays;
import java.awt.image.BufferedImage;

import collision.CollisionTag;

import entity.Thing;
import main.Debug;

public final class JScratch {
	
	// POSITION
	public static Action Forward(Object distance) { return new ForwardAction(distance); }
	public static Action MoveX(Object x) { return Change("x", x); }
	public static Action MoveY(Object y) { return Change("y", y); }
	public static Action Move(Object x, Object y) {
		return Parallel(Change("x", x), Change("y", y));
	}
	
	public static Action GoTo(Thing target) {
		return Parallel(
			Set("x", (Value) action -> target.getX()),
			Set("y", (Value) action -> target.getY())
		);
	}
	public static Action Warp(Thing target) { return GoTo(target); }
	public static Action GoTo(Object x, Object y) {
		return Parallel(Set("x", x), Set("y", y));
	}
	public static Action Warp(Object x, Object y) { return GoTo(x, y); }
	public static Action SetX(Object x) { return Set("x", x); }
	public static Action SetY(Object y) { return Set("y", y); }
	
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
	public static Action Look(Object angle) { return Set("angle", angle); }
	public static Action Turn(Object angle) { return Change("angle", angle); }
	// TWEEN: ANGLE
	public static Action LookTowards(Thing target, int frames) {
		return new TweenAction(
			"angle",
			Value.Get("angle"),
			(Value) action -> {
				ActionTarget owner = action.getOwner();

				float dx = target.getX() - owner.getX();
				float dy = target.getY() - owner.getY();

				return (float) Math.toDegrees(Math.atan2(dy, dx));
			},
			frames, Easing.Linear
		);
	}
	public static Action Look(Object angle, int frames) {
		return new TweenAction("angle",
			angle, frames, Easing.Linear
		);
	}
	public static Action Look(Object start, Object end, int frames) {
		return new TweenAction("angle",
			start, end, frames,
			Easing.Linear
		);
	}
	public static Action Turn(Object angle, int frames) {
		return new TweenAction(
			"angle",
			(Value) action -> Value.Get("angle"),
			(Value) action -> Add(Value.Get("angle"), angle),
			frames, Easing.Linear
		);
	}
	
	public static Action SetTrueAngle(Object angle) { return Set("trueAngle", angle); }
	public static Action ChangeTrueAngle(Object angle) { return Change("trueAngle", angle); }
	public static Action SetAppearAngle(Object angle) { return Set("appearAngle", angle); }
	public static Action ChangeAppearAngle(Object angle) { return Change("appearAngle", angle); }
	public static Action EnableAngleOverride() { return Set("angleOverride", true); }
	public static Action DisableAngleOverride() { return Set("angleOverride", false); }
	
	// APPEARANCE
	public static Action SetCostume(String name) { return new SetCostumeAction(name); }
	public static Action SetCostume(BufferedImage image) { return new SetCostumeAction(image); }
	
	public static Action SetColor(Object color) { return Set("color", color); }
	public static Action ChangeColor(Object amount) { return Change("color", amount); }

	public static Action SetDesaturation(Object desat) { return Set("desaturation", desat); }
	public static Action ChangeDesaturation(Object amount) { return Change("desaturation", amount); }
	
	public static Action SetPixelate(Object pixelate) { return Set("pixelate", pixelate); }
	public static Action ChangePixelate(Object amount) { return Change("pixelate", amount); }

	public static Action SetBrightness(Object brightness) { return Set("brightness", brightness); }
	public static Action ChangeBrightness(Object amount) { return Change("brightness", amount); }

	public static Action SetGhost(Object ghost) { return Set("ghost", ghost); }
	public static Action ChangeGhost(Object amount) { return Change("ghost", amount); }

	public static Action SetSize(Object size) { return Set("size", size); }
	public static Action ChangeSize(Object amount) { return Change("size", amount); }
	
	// HITBOX, ONLY FOR ENTITY
	public static Action AddCircleHitbox(String name, Object radius) { return new AddCircleHitbox(name, radius); }
	public static Action AddRectangleHitbox(String name, Object width, Object height) { return new AddRectangleHitbox(name, width, height); }

	public static Action EnableHitbox(String name) { return new SetHitboxEnabled(name, true); }
	public static Action DisableHitbox(String name) { return new SetHitboxEnabled(name, false); }

	public static Action AddHitboxTag(String name, Object... tags) { return AdmitHitboxTags(name, tags, true); }
	public static Action RemoveHitboxTag(String name, Object... tags) { return AdmitHitboxTags(name, tags, false); }
	private static Action AdmitHitboxTags(String name, Object[] tags, boolean state) {
		if (tags.length == 0) {
			throw Debug.terminate("JScratch", "At least one hitbox tag is required");
		}

		Action[] actions = new Action[tags.length];

		for (int i = 0; i < tags.length; i++) {
			actions[i] = new SetHitboxTag(
				name,
				CollisionTag.valueOf(tags[i].toString().toUpperCase(Locale.ROOT)),
				state
			);
		}

		return Sequence(actions);
	}
	
	// SOUND
	public static Action Sound(String name, String asset, float volume) {
		return Sequence(
			Sound(name, asset),
			SetSoundVolume(name, volume)
		);
	}
	public static Action Sound(String name, String path) { return new SoundAction(name, path); }
	public static Action SetSoundVolume(String name, Object volume) { return new SetSoundVolumeAction(name, volume); }
	public static Action PlaySound(String name) { return new PlaySoundAction(name); }
	
	// VARIABLE
	public static VariableAction Declare(String name, Object value) { return Var(name, value); }
	public static VariableAction Var(String name, Object value) { return new VariableAction( name, VariableAction.Operation.DECLARE, value); }
	public static VariableAction Set(String name, Object value) { return new VariableAction(name, VariableAction.Operation.SET, value); }
	public static VariableAction Change(String name, Object value) { return new VariableAction(name, VariableAction.Operation.CHANGE,value); }
	public static Value Get(String name) { return Value.Get(name); }
	public static Action Replicate(String... names) {
		Action[] actions = new Action[names.length];

		for (int i = 0; i < names.length; i++) {
			actions[i] = Var(names[i], Get(names[i]));
		}

		return Sequence(actions);
	}
	
	// BULLET LIFE
	public static Action SpawnBullet(Action action) { return new SpawnBulletAction(action); }
	public static Action Spawn(String className, String name) {
		return new SpawnAction(className, name);
	}
	public static Action Bestow(String name, ActionFactory factory) {
		return new BestowAction(name, factory);
	}
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
		Debug.warn("JScratch", "you're mispelling \"Parallel\"... Can't you spell?");
		return Parallel(actions);
	}
	public static Action Par(Action... actions) { return Parallel(actions); }
	public static Action Parallel(Action... actions) { return new Parallel(actions); }
	
	// CONTROL FLOW
	public static Action Wait(Object x) { return new WaitAction(x); }
	public static Action Wait() { return new WaitAction(1); }
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
			String temp = String.format(
					"unknown Forever mode: \"%s\"\nTry be sober. Defaulting to Sequence.", type);
			Debug.warn("JScratch", temp);

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
	public static Value Distance(Thing target) { return MathValue.Distance(target); }
	public static Value Distance(Object x, Object y) { return MathValue.Distance(x, y); }
	
	public static Value Abs(Object value) { return MathValue.Abs(value); }
	public static Value RoundUp(Object value) { return MathValue.RoundUp(value); }
	public static Value RoundDown(Object value) { return MathValue.RoundDown(value); }
	public static Value Min(Object... values) { return MathValue.Min(values); }
	public static Value Max(Object... values) { return MathValue.Max(values); }
	public static Value Avg(Object... values) { return MathValue.Avg(values); }
	
	public static Value Random() { return MathValue.Random(); }
	public static Value Random(Object a, Object b) { return MathValue.Random(a, b); }
	public static Value RandomSign() { return MathValue.RandomSign(); }
	
	// TWEENSERVICE, SYNTAX SUGAR TWEEN
	public static Action Tween(Object... args) {
		Easing easing = Easing.Linear;

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
		return new TweenAction("x",
			Value.Get("x"), end, frames,
			Easing.Linear
		);
	}
	private static Action TweenX(Object start, Object end, Object frames) {
		return new TweenAction("x",
			start, end, frames,
			Easing.Linear
		);
	}
	private static Action TweenY(Object end, Object frames) {
		return new TweenAction("y",
			Value.Get("y"), end, frames,
			Easing.Linear
		);
	}
	private static Action TweenY(Object start, Object end, Object frames) {
		return new TweenAction("y",
			start, end, frames,
			Easing.Linear
		);
	}
	
	
	// PRINT
	public static Action Print(Object message) { return new PrintAction(message, true); }
	public static Action LuaPrint(Object message) { return new PrintAction(message, false); }
}
