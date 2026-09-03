// FACADE and BRIDGE(Java, Lua)
package jsl;

import action.Action;
import action.ActionFactory;
import static action.JScratch.*;

import entity.Entity;

import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;
import org.luaj.vm2.lib.OneArgFunction;
import org.luaj.vm2.lib.TwoArgFunction;
import org.luaj.vm2.lib.VarArgFunction;
import org.luaj.vm2.lib.ZeroArgFunction;
import org.luaj.vm2.lib.jse.CoerceJavaToLua;
import org.luaj.vm2.lib.jse.JsePlatform;

public final class JSL {
	private JSL() {}
	
	private static Object toJava(LuaValue value) {
		if (value.isnumber()) {	return value.todouble();}
		if (value.isstring()) {	return value.tojstring();}
		if (value.isuserdata()) { return value.touserdata(); }

		return value;
	}
	private static Object[] toJava(Varargs args) {
		Object[] values = new Object[args.narg()];

		for (int i = 0; i < args.narg(); i++) {
			values[i] = toJava(args.arg(i + 1));
		}

		return values;
	}
	
	private static Action[] toActions(Varargs args) {
		Action[] actions = new Action[args.narg()];

		for (int i = 0; i < args.narg(); i++) {
			actions[i] = (Action) args.arg(i + 1).checkuserdata(Action.class);
		}

		return actions;
	}

	public static Globals registerJScratch() {
		Globals globals = JsePlatform.standardGlobals();
		globals.set("wait", new OneArgFunction() {
			@Override
			public LuaValue call(LuaValue value) {
				return CoerceJavaToLua.coerce( Wait( toJava(value) ) );
			}
		});
		
		globals.set("move", new TwoArgFunction() {
			@Override
			public LuaValue call(LuaValue x, LuaValue y) {
				return CoerceJavaToLua.coerce( Move( toJava(x), toJava(y) ) );
			}
		});
		globals.set("moveX", new OneArgFunction() {
			@Override
			public LuaValue call(LuaValue x) {
				return CoerceJavaToLua.coerce( MoveX( toJava(x) ) );
			}
		});
		
		globals.set("moveY", new OneArgFunction() {
			@Override
			public LuaValue call(LuaValue y) {
				return CoerceJavaToLua.coerce( MoveY( toJava(y)) );
			}
		});
		
		globals.set("goTo", new VarArgFunction() {
			@Override
			public Varargs invoke(Varargs args) {
				int count = args.narg();

				if (count == 1) {
					Entity target = (Entity) args.arg(1).checkuserdata(Entity.class);

					return CoerceJavaToLua.coerce( GoTo(target) );
				}
				if (count == 2) {
					return CoerceJavaToLua.coerce( GoTo( toJava(args.arg(1)), toJava(args.arg(2)) ) );
				}

				throw new IllegalArgumentException("goTo expects (Entity) or (x, y)");
			}
		});
		globals.set("warp", globals.get("goTo"));
		
		
		globals.set("setX", new OneArgFunction() {
			@Override
			public LuaValue call(LuaValue value) {
				return CoerceJavaToLua.coerce( SetX(  toJava(value) ) );
			}
		});
		
		globals.set("setY", new OneArgFunction() {
			@Override
			public LuaValue call(LuaValue value) {
				return CoerceJavaToLua.coerce( SetY(  toJava(value) ) );
			}
		});
		
		globals.set("forward", new OneArgFunction() {
			@Override
			public LuaValue call(LuaValue distance) {
				return CoerceJavaToLua.coerce( Forward( toJava(distance)) );
			}
		});
		
		globals.set("turn", new OneArgFunction() {
			@Override
			public LuaValue call(LuaValue angle) {
				return CoerceJavaToLua.coerce( Turn( toJava(angle)) );
			}
		});
		
		globals.set("look", new OneArgFunction() {
			@Override
			public LuaValue call(LuaValue angle) {
				return CoerceJavaToLua.coerce( Look( toJava(angle) ) );
			}
		});
		
		globals.set("lookTowards", new OneArgFunction() {
			@Override
			public LuaValue call(LuaValue value) {
				Entity target = (Entity) value.checkuserdata(Entity.class);

				return CoerceJavaToLua.coerce( LookTowards(target) );
			}
		});
		
		globals.set("addCircleHitbox", new TwoArgFunction() {
			@Override
			public LuaValue call(LuaValue name, LuaValue radius) {
				return CoerceJavaToLua.coerce(
					AddCircleHitbox(
						name.tojstring(),
						toJava(radius)
					)
				);
			}
		});
		
		globals.set("addRectangleHitbox", new VarArgFunction() {
			@Override
			public Varargs invoke(Varargs args) {
				return CoerceJavaToLua.coerce(
					AddRectangleHitbox(
						args.arg(1).tojstring(),
						toJava(args.arg(2)),
						toJava(args.arg(3))
					)
				);
			}
		});
		
		globals.set("enableHitbox", new OneArgFunction() {
			@Override
			public LuaValue call(LuaValue name) {
				return CoerceJavaToLua.coerce( EnableHitbox(name.tojstring()) );
			}
		});

		globals.set("disableHitbox", new OneArgFunction() {
			@Override
			public LuaValue call(LuaValue name) {
				return CoerceJavaToLua.coerce( DisableHitbox(name.tojstring()) );
			}
		});
		globals.set("addHitboxTag", new TwoArgFunction() {
			@Override
			public LuaValue call(LuaValue name, LuaValue tag) {
				return CoerceJavaToLua.coerce( AddHitboxTag( name.tojstring(), tag.tojstring() ) );
			}
		});

		globals.set("removeHitboxTag", new TwoArgFunction() {
			@Override
			public LuaValue call(LuaValue name, LuaValue tag) {
				return CoerceJavaToLua.coerce( RemoveHitboxTag(name.tojstring(), tag.tojstring() ) );
			}
		});

		globals.set("sound", new TwoArgFunction() {
			@Override
			public LuaValue call(LuaValue name, LuaValue path) {
				return CoerceJavaToLua.coerce( Sound( name.tojstring(), path.tojstring() ));
			}
		});

		globals.set("setsoundvolume", new TwoArgFunction() {
			@Override
			public LuaValue call(LuaValue name, LuaValue volume) {
				return CoerceJavaToLua.coerce( SetSoundVolume( name.tojstring(), ((Number) toJava(volume)).floatValue() ) );
			}
		});

		globals.set("playsound", new OneArgFunction() {
			@Override
			public LuaValue call(LuaValue name) {
				return CoerceJavaToLua.coerce( PlaySound( name.tojstring() ) );
			}
		});
		
		globals.set("var", new TwoArgFunction() {
			@Override
			public LuaValue call(LuaValue name, LuaValue value) {
				return CoerceJavaToLua.coerce( Declare(name.tojstring(),  toJava(value) ) );
			}
		});
		globals.set("declare", globals.get("var"));

		globals.set("vars", new VarArgFunction() {
			@Override
			public Varargs invoke(Varargs args) {
				if (args.narg() % 2 != 0) {
					throw new IllegalArgumentException("vars requires name/value pairs.");
				}

				Object[] values = new Object[args.narg()];

				for (int i = 0; i < args.narg(); i++) {
					values[i] = toJava(args.arg(i + 1));
				}

				return CoerceJavaToLua.coerce( Vars(values) );
			}
		});
		
		globals.set("set", new TwoArgFunction() {
			@Override
			public LuaValue call(LuaValue name, LuaValue value) {
				return CoerceJavaToLua.coerce( Set(name.tojstring(),  toJava(value) ) );
			}
		});

		globals.set("change", new TwoArgFunction() {
			@Override
			public LuaValue call(LuaValue name, LuaValue value) {
				return CoerceJavaToLua.coerce( Change(name.tojstring(),  toJava(value) ) );
			}
		});
		
		globals.set("get", new OneArgFunction() {
			@Override
			public LuaValue call(LuaValue name) {
				return CoerceJavaToLua.coerce( Get(name.tojstring()) );
			}
		});
		
		globals.set("spawnBullet", new VarArgFunction() {
			@Override
			public Varargs invoke(Varargs args) {
				int count = args.narg();

				if (count == 1) {
					Action action = (Action) args.arg(1).checkuserdata(Action.class);
					return CoerceJavaToLua.coerce( SpawnBullet(action) );
				}
				if (count == 4) {
					double x = args.arg(1).todouble();
					double y = args.arg(2).todouble();
					double angle = args.arg(3).todouble();

					Action action = (Action) args.arg(4).checkuserdata(Action.class);
					return CoerceJavaToLua.coerce( SpawnBullet(x, y, angle, action) );
				}

				throw new IllegalArgumentException(
					"SpawnBullet expects (action) or " +
					"(x, y, angle, action)"
				);
			}
		});
		
		globals.set("destroy", new ZeroArgFunction() {
			@Override
			public LuaValue call() {
				return CoerceJavaToLua.coerce( Destroy() );
			}
		});
		
		LuaValue sequence = new VarArgFunction() {
			@Override
			public Varargs invoke(Varargs args) {
				return CoerceJavaToLua.coerce( Sequence( toActions(args) ) );
			}
		};
		globals.set("seq", sequence);
		globals.set("sequence", sequence);
		
		globals.set("paralell", new VarArgFunction() {
			@Override
			public Varargs invoke(Varargs args) {
				return CoerceJavaToLua.coerce( Paralell( toActions(args) ) );
			}
		});
		LuaValue parallel = new VarArgFunction() {
			@Override
			public Varargs invoke(Varargs args) {
				return CoerceJavaToLua.coerce( Parallel( toActions(args) ) );
			}
		};
		globals.set("par", parallel);
		globals.set("parallel", parallel);
		
		globals.set("forever", new VarArgFunction() {
			@Override
			public Varargs invoke(Varargs args) {
				Object type = toJava(args.arg(1));
				Varargs actionArgs = args.subargs(2);

				return CoerceJavaToLua.coerce(Forever(type, toActions(actionArgs)));
			}
		});
		
		globals.set("jsfor", new VarArgFunction() {
			@Override
			public Varargs invoke(Varargs args) {
				String variable = args.arg(1).tojstring();
				Object start = toJava(args.arg(2));
				Object end = toJava(args.arg(3));

				LuaValue function = args.arg(4);
				function.checkfunction();

				ActionFactory factory = () -> {
					LuaValue result = function.call();

					return (Action) result.checkuserdata(Action.class);
				};

				return CoerceJavaToLua.coerce( For(variable, start, end, factory) );
			}
		});
		
		
		globals.set("add", new VarArgFunction() {
			@Override
			public Varargs invoke(Varargs args) {
				return CoerceJavaToLua.coerce( Add( toJava(args) ) );
			}
		});

		globals.set("sub", new VarArgFunction() {
			@Override
			public Varargs invoke(Varargs args) {
				return CoerceJavaToLua.coerce( Sub( toJava(args) ) );
			}
		});

		globals.set("mul", new VarArgFunction() {
			@Override
			public Varargs invoke(Varargs args) {
				return CoerceJavaToLua.coerce( Mul( toJava(args) ) );
			}
		});

		globals.set("div", new VarArgFunction() {
			@Override
			public Varargs invoke(Varargs args) {
				return CoerceJavaToLua.coerce( Div( toJava(args) ) );
			}
		});
		
		globals.set("mod", new VarArgFunction() {
			@Override
			public Varargs invoke(Varargs args) {
				return CoerceJavaToLua.coerce( Mod( toJava(args) ) );
			}
		});

		globals.set("power", new TwoArgFunction() {
			@Override
			public LuaValue call(LuaValue a, LuaValue b) {
				return CoerceJavaToLua.coerce( Power(toJava(a), toJava(b)));
			}
		});
		globals.set("pow", globals.get("power"));

		globals.set("root", new TwoArgFunction() {
			@Override
			public LuaValue call(LuaValue a, LuaValue b) {
				return CoerceJavaToLua.coerce(
					Root(toJava(a), toJava(b))
				);
			}
		});

		globals.set("min", new VarArgFunction() {
			@Override
			public Varargs invoke(Varargs args) {
				return CoerceJavaToLua.coerce( Min( toJava(args) ) );
			}
		});

		globals.set("max", new VarArgFunction() {
			@Override
			public Varargs invoke(Varargs args) {
				return CoerceJavaToLua.coerce( Max( toJava(args) ) );
			}
		});
		
		globals.set("random", new VarArgFunction() {
			@Override
			public Varargs invoke(Varargs args) {
				int count = args.narg();

				if (count == 0) {
					return CoerceJavaToLua.coerce( Random() );
				}

				if (count == 2) {
					return CoerceJavaToLua.coerce(
						Random( toJava(args.arg(1)), toJava(args.arg(2)) )
					);
				}

				throw new IllegalArgumentException( "random expects () or (a, b)" );
			}
		});
		
		globals.set("jsprint", new OneArgFunction() {
			@Override
			public LuaValue call(LuaValue value) {
				return CoerceJavaToLua.coerce( LuaPrint( toJava(value) ) );
			}
		});
		return globals;
	}
}