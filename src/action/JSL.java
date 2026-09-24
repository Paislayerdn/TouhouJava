// FACADE and BRIDGE( Java, Lua )
package action;

import java.util.function.Function;
import java.util.function.BiFunction;
import java.util.function.Supplier;

import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;
import org.luaj.vm2.lib.*;
import org.luaj.vm2.lib.jse.CoerceJavaToLua;
import org.luaj.vm2.lib.jse.JsePlatform;

import static action.JScratch.*;

import entity.Entity;

public final class JSL {
	private JSL() {}
	
	private static Object toJava( LuaValue value ) {
		if ( value.isnumber() ) { return value.tofloat();}
		if ( value.isstring() ) { return value.tojstring();}
		if ( value.isuserdata() ) { return value.touserdata(); }

		return value;
	}
	private static Object[] toJava( Varargs args ) {
		Object[] values = new Object[args.narg()];

		for (int i = 0; i < args.narg(); i++) {
			values[i] = toJava( args.arg(i + 1) );
		}

		return values;
	}
	
	private static Action[] toActions( Varargs args ) {
		Action[] actions = new Action[args.narg()];

		for (int i = 0; i < args.narg(); i++) {
			actions[i] = ( Action ) args.arg(i + 1).checkuserdata( Action.class );
		}

		return actions;
	}
	
	private static void registerZeroArgAction(Globals globals,
		String name, Supplier<Action> function
	) {
		globals.set(name, new ZeroArgFunction() {
			@Override
			public LuaValue call() {
				return CoerceJavaToLua.coerce(function.get());
			}
		});
	}
	private static void registerOneArgAction(Globals globals,
		String name, Function<Object, Action> function
	) {
		globals.set(name, new OneArgFunction() {
			@Override
			public LuaValue call(LuaValue value) {
				return CoerceJavaToLua.coerce(
					function.apply(toJava(value))
				);
			}
		});
	}
	private static void registerTwoArgAction(Globals globals,
		String name, BiFunction<Object, Object, Action> function
	) {
		globals.set(name, new TwoArgFunction() {
			@Override
			public LuaValue call(LuaValue a, LuaValue b) {
				return CoerceJavaToLua.coerce(
					function.apply(toJava(a), toJava(b))
				);
			}
		});
	}
	private static void registerVarArgAction(Globals globals,
		String name, Function<Object[], Action> function
	) {
		globals.set(name, new VarArgFunction() {
			@Override
			public Varargs invoke(Varargs args) {
				return CoerceJavaToLua.coerce(
					function.apply(toJava(args))
				);
			}
		});
	}
	private static <T> void registerZeroArg(Globals globals,
		String name, Supplier<T> function
	) {
		globals.set(name, new ZeroArgFunction() {
			@Override
			public LuaValue call() {
				return CoerceJavaToLua.coerce(function.get());
			}
		});
	}

	private static <T> void registerOneArg(Globals globals,
		String name, Function<Object, T> function
	) {
		globals.set(name, new OneArgFunction() {
			@Override
			public LuaValue call(LuaValue value) {
				return CoerceJavaToLua.coerce(
					function.apply(toJava(value))
				);
			}
		});
	}

	private static <T> void registerTwoArg(Globals globals,
		String name, BiFunction<Object, Object, T> function
	) {
		globals.set(name, new TwoArgFunction() {
			@Override
			public LuaValue call(LuaValue a, LuaValue b) {
				return CoerceJavaToLua.coerce(
					function.apply(toJava(a), toJava(b))
				);
			}
		});
	}

	private static <T> void registerVarArg(Globals globals,
		String name, Function<Object[], T> function
	) {
		globals.set(name, new VarArgFunction() {
			@Override
			public Varargs invoke(Varargs args) {
				return CoerceJavaToLua.coerce(
					function.apply(toJava(args))
				);
			}
		});
	}
	private static <T> void registerStringObject(Globals globals,
		String name, BiFunction<String, Object, T> function
	) {
		globals.set(name, new TwoArgFunction() {
			@Override
			public LuaValue call(LuaValue a, LuaValue b) {
				return CoerceJavaToLua.coerce(
					function.apply(a.tojstring(), toJava(b))
				);
			}
		});
	}
	private static <T> void registerStringArg(Globals globals,
		String name, Function<String, T> function
	) {
		globals.set(name, new OneArgFunction() {
			@Override
			public LuaValue call(LuaValue value) {
				return CoerceJavaToLua.coerce(
					function.apply(value.tojstring())
				);
			}
		});
	}
	private static <T> void registerStringString(Globals globals,
		String name,
		BiFunction<String, String, T> function
	) {
		globals.set(name, new TwoArgFunction() {
			@Override
			public LuaValue call(LuaValue a, LuaValue b) {
				return CoerceJavaToLua.coerce(
					function.apply(a.tojstring(), b.tojstring())
				);
			}
		});
	}

	public static Globals registerJScratch() {
		Globals globals = JsePlatform.standardGlobals();
		
		// POSITION
		registerOneArgAction(globals, "moveX", JScratch::MoveX);
		registerOneArgAction(globals, "moveY", JScratch::MoveY);
		registerTwoArgAction(globals, "move", JScratch::Move);
		registerOneArgAction(globals, "forward", JScratch::Forward);
		
		globals.set("goTo", new VarArgFunction() {
			@Override
			public Varargs invoke(Varargs args) {
				int count = args.narg();

				if (count == 1) {
					Entity target = (Entity) args.arg(1).checkuserdata(Entity.class);

					return CoerceJavaToLua.coerce(GoTo(target));
				}

				if (count == 2) {
					return CoerceJavaToLua.coerce(
						GoTo( toJava(args.arg(1)), toJava(args.arg(2)) )
					);
				}

				if (count == 3) {
					if (args.arg(1).isuserdata()) {
						Entity target = (Entity) args.arg(1).checkuserdata(Entity.class);

						return CoerceJavaToLua.coerce( GoTo(target, args.arg(2).checkint()) );
					}

					return CoerceJavaToLua.coerce(
						GoTo( toJava(args.arg(1)), toJava(args.arg(2)), args.arg(3).checkint() )
					);
				}

				if (count == 5) {
					return CoerceJavaToLua.coerce(
						GoTo(
							toJava(args.arg(1)),
							toJava(args.arg(2)),
							toJava(args.arg(3)),
							toJava(args.arg(4)),
							args.arg(5).checkint()
						)
					);
				}

				throw new IllegalArgumentException(
					"goTo expects (Entity), (x,y), "
					+ "(Entity,frames), (endX,endY,frames), "
					+ "or (startX,startY,endX,endY,frames)"
				);
			}
		});
		globals.set("warp", globals.get("goTo"));
		
		globals.set("setX", new VarArgFunction() {
			@Override
			public Varargs invoke(Varargs args) {
				if (args.narg() == 1) {
					return CoerceJavaToLua.coerce( SetX( toJava(args.arg(1)) ) );
				}
				if (args.narg() == 2) {
					return CoerceJavaToLua.coerce(
						SetX( toJava(args.arg(1)), args.arg(2).checkint() )
					);
				}
				if (args.narg() == 3) {
					return CoerceJavaToLua.coerce(
						SetX(
							toJava(args.arg(1)),
							toJava(args.arg(2)),
							args.arg(3).checkint()
						)
					);
				}

				throw new IllegalArgumentException(
					"setX expects (x), (endX, frames), or (startX, endX, frames)"
				);
			}
		} );

		globals.set("setY", new VarArgFunction() {
			@Override
			public Varargs invoke(Varargs args) {
				if (args.narg() == 1) {
					return CoerceJavaToLua.coerce( SetY( toJava(args.arg(1)) ) );
				}
				if (args.narg() == 2) {
					return CoerceJavaToLua.coerce(
						SetY( toJava(args.arg(1)), args.arg(2).checkint() )
					);
				}
				if (args.narg() == 3) {
					return CoerceJavaToLua.coerce(
						SetY(
							toJava(args.arg(1)),
							toJava(args.arg(2)),
							args.arg(3).checkint()
						)
					);
				}

				throw new IllegalArgumentException(
					"setY expects (y), (endY, frames), or (startY, endY, frames)"
				);
			}
		} );
		
		
		// ANGLE, THE FIRST 3 ALWAYS FOLLOW ANGLEOVERRIDE
		globals.set("look", new VarArgFunction() {
			@Override
			public Varargs invoke(Varargs args) {
				if (args.narg() == 1) {
					return CoerceJavaToLua.coerce( Look( toJava(args.arg(1)) ) );
				}

				if (args.narg() == 2) {
					return CoerceJavaToLua.coerce(
						Look( toJava(args.arg(1)), args.arg(2).checkint() )
					);
				}

				if (args.narg() == 3) {
					return CoerceJavaToLua.coerce(
						Look(
							toJava(args.arg(1)),
							toJava(args.arg(2)),
							args.arg(3).checkint()
						)
					);
				}

				throw new IllegalArgumentException(
					"look expects (angle), (end angle, frames), "
					+ "or (start angle, end angle, frames)"
				);
			}
		});

		globals.set("turn", new VarArgFunction() {
			@Override
			public Varargs invoke(Varargs args) {
				if (args.narg() == 1) {
					return CoerceJavaToLua.coerce(
						Turn(toJava(args.arg(1)))
					);
				}

				if (args.narg() == 2) {
					return CoerceJavaToLua.coerce(
						Turn(
							toJava(args.arg(1)),
							args.arg(2).checkint()
						)
					);
				}

				throw new IllegalArgumentException(
					"turn expects (angle) or (end angle, frames)"
				);
			}
		});

		globals.set("lookTowards", new VarArgFunction() {
			@Override
			public Varargs invoke(Varargs args) {
				if (args.narg() == 1) {
					Entity target =
						(Entity) args.arg(1).checkuserdata(Entity.class);

					return CoerceJavaToLua.coerce(
						LookTowards(target)
					);
				}

				if (args.narg() == 2) {
					Entity target =
						(Entity) args.arg(1).checkuserdata(Entity.class);

					return CoerceJavaToLua.coerce(
						LookTowards(target, args.arg(2).checkint())
					);
				}

				throw new IllegalArgumentException(
					"lookTowards expects (Entity) or (Entity, frames)"
				);
			}
		});
		
		registerOneArgAction(globals, "setTrueAngle", JScratch::SetTrueAngle);
		registerOneArgAction(globals, "changeTrueAngle", JScratch::ChangeTrueAngle);
		registerOneArgAction(globals, "setAppearAngle", JScratch::SetAppearAngle);
		registerOneArgAction(globals, "changeAppearAngle", JScratch::ChangeAppearAngle);
		registerZeroArgAction(globals, "enableAngleOverride", JScratch::EnableAngleOverride);
		registerZeroArgAction(globals, "disableAngleOverride", JScratch::DisableAngleOverride);
		
		// APPEARANCE
		registerStringArg(globals, "setCostume", JScratch::SetCostume);
		registerOneArgAction(globals, "setColor", JScratch::SetColor);
		registerOneArgAction(globals, "changeColor", JScratch::ChangeColor);
		registerOneArgAction(globals, "setPixelate", JScratch::SetPixelate);
		registerOneArgAction(globals, "changePixelate", JScratch::ChangePixelate);
		registerOneArgAction(globals, "setBrightness", JScratch::SetBrightness);
		registerOneArgAction(globals, "changeBrightness", JScratch::ChangeBrightness);
		registerOneArgAction(globals, "setGhost", JScratch::SetGhost);
		registerOneArgAction(globals, "changeGhost", JScratch::ChangeGhost);
		registerOneArgAction(globals, "setSize", JScratch::SetSize);
		registerOneArgAction(globals, "changeSize", JScratch::ChangeSize);
		
		// HITBOX, ONLY FOR ENTITY
		globals.set( "addCircleHitbox", new TwoArgFunction() {
			@Override
			public LuaValue call( LuaValue name, LuaValue radius ) {
				return CoerceJavaToLua.coerce( 
					AddCircleHitbox( name.tojstring(), toJava( radius ) )
				);
			}
		} );
		registerVarArg(globals, "addRectangleHitbox", args -> 
			AddRectangleHitbox((String) args[0], args[1], args[2])
		);

		registerStringArg(globals, "enableHitbox", JScratch::EnableHitbox);
		registerStringArg(globals, "disableHitbox", JScratch::DisableHitbox);

		registerStringString(globals, "addHitboxTag", JScratch::AddHitboxTag);
		registerStringString(globals, "removeHitboxTag", JScratch::RemoveHitboxTag);

		// SOUND
		registerStringString(globals, "sound", JScratch::Sound);
		globals.set( "setSoundVolume", new TwoArgFunction() {
			@Override
			public LuaValue call( LuaValue name, LuaValue volume ) {
				return CoerceJavaToLua.coerce( SetSoundVolume( name.tojstring(), ( ( Number ) toJava( volume ) ).floatValue() ) );
			}
		} );
		registerStringArg(globals, "playSound", JScratch::PlaySound);
		
		// VARIABLE
		registerStringObject(globals, "var", JScratch::Declare);
		globals.set("declare", globals.get("var"));

		registerStringObject(globals, "set", JScratch::Set);
		registerStringObject(globals, "change", JScratch::Change);
		registerStringArg(globals, "get", JScratch::Get);
		
		// BULLET LIFE
		globals.set("spawnBullet", new OneArgFunction() {
			@Override
			public LuaValue call(LuaValue value) {
				Action action = (Action) value.checkuserdata(Action.class);

				return CoerceJavaToLua.coerce(
					SpawnBullet(action)
				);
			}
		});
		registerZeroArgAction(globals, "destroy", JScratch::Destroy);
		
		// LOGIC
		registerVarArg(globals, "jsand", JScratch::And);
		registerVarArg(globals, "jsor", JScratch::Or);
		registerOneArg(globals, "jsnot", JScratch::Not);

		// COMPARISON
		registerVarArg(globals, "greater", JScratch::Greater);
		registerVarArg(globals, "greaterEqual", JScratch::GreaterEqual);
		registerVarArg(globals, "less", JScratch::Less);
		registerVarArg(globals, "lessEqual", JScratch::LessEqual);
		registerVarArg(globals, "equal", JScratch::Equal);
		
		// PROCESS CONTROL
		LuaValue sequence = new VarArgFunction() {
			@Override
			public Varargs invoke( Varargs args ) {
				return CoerceJavaToLua.coerce( Sequence( toActions( args ) ) );
			}
		};
		globals.set( "seq", sequence );
		globals.set( "sequence", sequence );
		
		globals.set( "paralell", new VarArgFunction() {
			@Override
			public Varargs invoke( Varargs args ) {
				return CoerceJavaToLua.coerce( Paralell( toActions( args ) ) );
			}
		} );
		LuaValue parallel = new VarArgFunction() {
			@Override
			public Varargs invoke( Varargs args ) {
				return CoerceJavaToLua.coerce( Parallel( toActions( args ) ) );
			}
		};
		globals.set( "par", parallel );
		globals.set( "parallel", parallel );
		
		// CONTROL FLOW
		globals.set( "wait", new VarArgFunction() {
			@Override
			public Varargs invoke( Varargs args ) {
				if ( args.narg() == 0 ) {
					return CoerceJavaToLua.coerce( Wait() );
				}

				return CoerceJavaToLua.coerce( Wait( toJava( args.arg1() ) ) );
			}
		} );
		registerOneArgAction(globals, "waitUntil", JScratch::WaitUntil);
		globals.set( "jsif", new VarArgFunction() {
			@Override
			public Varargs invoke( Varargs args ) {
				Object condition = toJava( args.arg(1) );

				LuaValue thenFunction = args.arg(2);
				thenFunction.checkfunction();

				ActionFactory thenFactory = () -> {
					LuaValue result = thenFunction.call();

					return ( Action ) result.checkuserdata( Action.class );
				};

				if (args.narg() >= 3) {
					LuaValue elseFunction = args.arg(3);
					elseFunction.checkfunction();

					ActionFactory elseFactory = () -> {
						LuaValue result = elseFunction.call();

						return ( Action ) result.checkuserdata( Action.class );
					};

					return CoerceJavaToLua.coerce( 
						If( condition, thenFactory, elseFactory )
					 );
				}

				return CoerceJavaToLua.coerce( 
					If( condition, thenFactory )
				 );
			}
		} );
		
		globals.set( "jsfor", new VarArgFunction() {
			@Override
			public Varargs invoke( Varargs args ) {
				String variable = args.arg(1).tojstring();
				Object start = toJava( args.arg(2) );
				Object end = toJava( args.arg(3) );

				LuaValue function = args.arg(4);
				function.checkfunction();

				ActionFactory factory = () -> {
					LuaValue result = function.call();

					return ( Action ) result.checkuserdata( Action.class );
				};

				return CoerceJavaToLua.coerce( For( variable, start, end, factory ) );
			}
		} );
		globals.set( "jswhile", new TwoArgFunction() {
			@Override
			public LuaValue call( LuaValue condition, LuaValue function ) {
				function.checkfunction();

				ActionFactory factory = () -> {
					LuaValue result = function.call();

					return ( Action ) result.checkuserdata( Action.class );
				};

				return CoerceJavaToLua.coerce( 
					While( toJava( condition ), factory )
				 );
			}
		} );
		globals.set( "repeatUntil", new TwoArgFunction() {
			@Override
			public LuaValue call( LuaValue condition, LuaValue function ) {
				function.checkfunction();

				ActionFactory factory = () -> {
					LuaValue result = function.call();

					return ( Action ) result.checkuserdata( Action.class );
				};

				return CoerceJavaToLua.coerce( 
					RepeatUntil( toJava( condition ), factory )
				 );
			}
		} );
		globals.set( "forever", new VarArgFunction() {
			@Override
			public Varargs invoke( Varargs args ) {
				Object type = toJava( args.arg(1) );
				Varargs actionArgs = args.subargs(2);

				return CoerceJavaToLua.coerce( Forever( type, toActions( actionArgs ) ) );
			}
		} );
		
		
		// MATH
		registerVarArg(globals, "add", JScratch::Add);
		registerVarArg(globals, "sub", JScratch::Sub);
		registerVarArg(globals, "mul", JScratch::Mul);
		registerVarArg(globals, "div", JScratch::Div);
		registerVarArg(globals, "mod", JScratch::Mod);
		registerTwoArg(globals, "power", JScratch::Power);
		globals.set("pow", globals.get("power"));
		registerTwoArg(globals, "root", JScratch::Root);
		registerOneArg(globals, "abs", JScratch::Abs);
		registerVarArg(globals, "min", JScratch::Min);
		registerVarArg(globals, "max", JScratch::Max);
		globals.set("random", new VarArgFunction() {
			@Override
			public Varargs invoke(Varargs args) {
				if (args.narg() == 0) {
					return CoerceJavaToLua.coerce(Random());
				}

				if (args.narg() == 2) {
					return CoerceJavaToLua.coerce(
						Random(toJava(args.arg(1)), toJava(args.arg(2)))
					);
				}

				throw new IllegalArgumentException("random expects () or (a, b)");
			}
		});
		registerZeroArg(globals, "randomSign", JScratch::RandomSign);
		
		// TWEENSERVICE
		LuaTable easing = new LuaTable();

		easing.set("linear", CoerceJavaToLua.coerce(Easing.LINEAR));
		easing.set("quadIn", CoerceJavaToLua.coerce(Easing.QUAD_IN));
		easing.set("quadOut", CoerceJavaToLua.coerce(Easing.QUAD_OUT));
		easing.set("quadInOut", CoerceJavaToLua.coerce(Easing.QUAD_IN_OUT));
		
		globals.set("easing", easing);
		registerVarArg(globals, "tween", JScratch::Tween);
		
		// PRINT
		registerOneArg(globals, "jsprint", JScratch::LuaPrint);
		
		// LUA SPECIFIC STUFFS
		globals.set("at", new VarArgFunction() {
			@Override
			public Varargs invoke(Varargs args) {
				LuaValue table = args.arg(1);
				Object index = toJava(args.arg(2));

				Value value = action -> {
					Object resolvedIndex = index instanceof Value? ((Value) index).get(action): index;

					if (!(resolvedIndex instanceof Number)) {
						throw JSCDebug.error("Lua",
							"Lua table index must resolve to a number: " + resolvedIndex
						);
					}

					int i = ((Number) resolvedIndex).intValue();

					return toJava(table.get(i));
				};

				return CoerceJavaToLua.coerce(value);
			}
		} );
		globals.set("callAt", new VarArgFunction() {
			@Override
			public Varargs invoke(Varargs args) {
				LuaValue table = args.arg(1);
				Object index = toJava(args.arg(2));

				return CoerceJavaToLua.coerce(
					new LuaCallAction(table, index)
				);
			}
		} );
		
		return globals;
	}
}