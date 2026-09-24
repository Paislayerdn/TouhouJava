// FACADE and BRIDGE( Java, Lua )
package action;

import java.util.function.Function;

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
	
	private static void registerOneArgAction(
		Globals globals,
		String name,
		Function<Object, Action> function
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

	public static Globals registerJScratch() {
		Globals globals = JsePlatform.standardGlobals();
		
		// POSITION
		globals.set( "moveX", new OneArgFunction() {
			@Override
			public LuaValue call( LuaValue x ) {
				return CoerceJavaToLua.coerce( MoveX( toJava(x) ) );
			}
		} );
		
		globals.set( "moveY", new OneArgFunction() {
			@Override
			public LuaValue call( LuaValue y ) {
				return CoerceJavaToLua.coerce( MoveY( toJava(y) ) );
			}
		} );
		globals.set( "move", new TwoArgFunction() {
			@Override
			public LuaValue call( LuaValue x, LuaValue y ) {
				return CoerceJavaToLua.coerce( Move( toJava(x), toJava(y) ) );
			}
		} );
		
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
		
		globals.set( "forward", new OneArgFunction() {
			@Override
			public LuaValue call( LuaValue distance ) {
				return CoerceJavaToLua.coerce( Forward( toJava( distance ) ) );
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
		
		globals.set( "setTrueAngle", new OneArgFunction() {
			@Override
			public LuaValue call( LuaValue value ) {
				return CoerceJavaToLua.coerce( SetTrueAngle( toJava( value ) ) );
			}
		} );

		globals.set( "changeTrueAngle", new OneArgFunction() {
			@Override
			public LuaValue call( LuaValue value ) {
				return CoerceJavaToLua.coerce( ChangeTrueAngle( toJava( value ) ) );
			}
		} );
		
		// ONLY FOR ENTITY, WARNING FOR USAGE UPON THING
		globals.set( "setAppearAngle", new OneArgFunction() {
			@Override
			public LuaValue call( LuaValue value ) {
				return CoerceJavaToLua.coerce( SetAppearAngle( toJava( value ) ) );
			}
		} );
		globals.set( "changeAppearAngle", new OneArgFunction() {
			@Override
			public LuaValue call( LuaValue value ) {
				return CoerceJavaToLua.coerce( ChangeAppearAngle( toJava( value ) ) );
			}
		} );
		globals.set( "enableAngleOverride", new ZeroArgFunction() {
			@Override
			public LuaValue call() {
				return CoerceJavaToLua.coerce( EnableAngleOverride() );
			}
		} );
		globals.set( "disableAngleOverride", new ZeroArgFunction() {
			@Override
			public LuaValue call() {
				return CoerceJavaToLua.coerce( DisableAngleOverride() );
			}
		} );
		
		// APPEARANCE
		globals.set("setCostume", new OneArgFunction() {
			@Override // ONLY FOR STRING
			public LuaValue call(LuaValue name) {
				return CoerceJavaToLua.coerce(SetCostume(name.tojstring()));
			}
		});

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
		globals.set( "addRectangleHitbox", new VarArgFunction() {
			@Override
			public Varargs invoke( Varargs args ) {
				return CoerceJavaToLua.coerce( 
					AddRectangleHitbox( 
						args.arg(1).tojstring(),
						toJava( args.arg(2) ),
						toJava( args.arg(3) )
					 )
				 );
			}
		} );
		
		globals.set( "enableHitbox", new OneArgFunction() {
			@Override
			public LuaValue call( LuaValue name ) {
				return CoerceJavaToLua.coerce( EnableHitbox( name.tojstring() ) );
			}
		} );

		globals.set( "disableHitbox", new OneArgFunction() {
			@Override
			public LuaValue call( LuaValue name ) {
				return CoerceJavaToLua.coerce( DisableHitbox( name.tojstring() ) );
			}
		} );
		
		globals.set( "addHitboxTag", new TwoArgFunction() {
			@Override
			public LuaValue call( LuaValue name, LuaValue tag ) {
				return CoerceJavaToLua.coerce( AddHitboxTag( name.tojstring(), tag.tojstring() ) );
			}
		} );

		globals.set( "removeHitboxTag", new TwoArgFunction() {
			@Override
			public LuaValue call( LuaValue name, LuaValue tag ) {
				return CoerceJavaToLua.coerce( RemoveHitboxTag( name.tojstring(), tag.tojstring() ) );
			}
		} );

		// SOUND
		globals.set( "sound", new TwoArgFunction() {
			@Override
			public LuaValue call( LuaValue name, LuaValue path ) {
				return CoerceJavaToLua.coerce( Sound( name.tojstring(), path.tojstring() ) );
			}
		} );
		globals.set( "setSoundVolume", new TwoArgFunction() {
			@Override
			public LuaValue call( LuaValue name, LuaValue volume ) {
				return CoerceJavaToLua.coerce( SetSoundVolume( name.tojstring(), ( ( Number ) toJava( volume ) ).floatValue() ) );
			}
		} );
		globals.set( "playSound", new OneArgFunction() {
			@Override
			public LuaValue call( LuaValue name ) {
				return CoerceJavaToLua.coerce( PlaySound( name.tojstring() ) );
			}
		} );
		
		// VARIABLE
		globals.set( "var", new TwoArgFunction() {
			@Override
			public LuaValue call( LuaValue name, LuaValue value ) {
				return CoerceJavaToLua.coerce( Declare( name.tojstring(), toJava( value ) ) );
			}
		} );
		globals.set( "declare", globals.get( "var" ) );
		globals.set( "set", new TwoArgFunction() {
			@Override
			public LuaValue call( LuaValue name, LuaValue value ) {
				return CoerceJavaToLua.coerce( Set( name.tojstring(), toJava( value ) ) );
			}
		} );
		globals.set( "change", new TwoArgFunction() {
			@Override
			public LuaValue call( LuaValue name, LuaValue value ) {
				return CoerceJavaToLua.coerce( Change( name.tojstring(), toJava( value ) ) );
			}
		} );
		globals.set( "get", new OneArgFunction() {
			@Override
			public LuaValue call( LuaValue name ) {
				return CoerceJavaToLua.coerce( Get( name.tojstring() ) );
			}
		} );
		
		// BULLET LIFE
		globals.set( "spawnBullet", new VarArgFunction() {
			@Override
			public Varargs invoke( Varargs args ) {
				int count = args.narg();

				if (count == 1) {
					Action action = ( Action ) args.arg(1).checkuserdata( Action.class );
					return CoerceJavaToLua.coerce( SpawnBullet( action ) );
				}
				
				throw new IllegalArgumentException( "SpawnBullet expects (action)" );
			}
		} );
		globals.set( "destroy", new ZeroArgFunction() {
			@Override
			public LuaValue call() {
				return CoerceJavaToLua.coerce( Destroy() );
			}
		} );
		
		// LOGIC
		globals.set( "jsand", new VarArgFunction() {
			@Override
			public Varargs invoke( Varargs args ) {
				Object[] values = toJava( args );
				return CoerceJavaToLua.coerce( And( values ) );
			}
		} );

		globals.set( "jsor", new VarArgFunction() {
			@Override
			public Varargs invoke( Varargs args ) {
				Object[] values = toJava( args );
				return CoerceJavaToLua.coerce( Or( values ) );
			}
		} );

		globals.set( "jsnot", new OneArgFunction() {
			@Override
			public LuaValue call( LuaValue value ) {
				return CoerceJavaToLua.coerce( Not( toJava( value ) ) );
			}
		} );

		// COMPARISON
		globals.set( "greater", new VarArgFunction() {
			@Override
			public Varargs invoke( Varargs args ) {
				Object[] values = toJava( args );
				return CoerceJavaToLua.coerce( Greater( values ) );
			}
		} );
		globals.set( "greaterEqual", new VarArgFunction() {
			@Override
			public Varargs invoke( Varargs args ) {
				Object[] values = toJava( args );
				return CoerceJavaToLua.coerce( GreaterEqual( values ) );
			}
		} );
		globals.set( "less", new VarArgFunction() {
			@Override
			public Varargs invoke( Varargs args ) {
				Object[] values = toJava( args );
				return CoerceJavaToLua.coerce( Less( values ) );
			}
		} );
		globals.set( "lessEqual", new VarArgFunction() {
			@Override
			public Varargs invoke( Varargs args ) {
				Object[] values = toJava( args );
				return CoerceJavaToLua.coerce( LessEqual( values ) );
			}
		} );
		globals.set( "equal", new VarArgFunction() {
			@Override
			public Varargs invoke( Varargs args ) {
				Object[] values = toJava( args );
				return CoerceJavaToLua.coerce( Equal( values ) );
			}
		} );
		
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
		globals.set( "waitUntil", new OneArgFunction() {
			@Override
			public LuaValue call( LuaValue condition ) {
				return CoerceJavaToLua.coerce( 
					WaitUntil( toJava( condition ) )
				 );
			}
		} );
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
		globals.set( "add", new VarArgFunction() {
			@Override
			public Varargs invoke( Varargs args ) {
				return CoerceJavaToLua.coerce( Add( toJava( args ) ) );
			}
		} );

		globals.set( "sub", new VarArgFunction() {
			@Override
			public Varargs invoke( Varargs args ) {
				return CoerceJavaToLua.coerce( Sub( toJava( args ) ) );
			}
		} );

		globals.set( "mul", new VarArgFunction() {
			@Override
			public Varargs invoke( Varargs args ) {
				return CoerceJavaToLua.coerce( Mul( toJava( args ) ) );
			}
		} );

		globals.set( "div", new VarArgFunction() {
			@Override
			public Varargs invoke( Varargs args ) {
				return CoerceJavaToLua.coerce( Div( toJava( args ) ) );
			}
		} );
		
		globals.set( "mod", new VarArgFunction() {
			@Override
			public Varargs invoke( Varargs args ) {
				return CoerceJavaToLua.coerce( Mod( toJava( args ) ) );
			}
		} );

		globals.set( "power", new TwoArgFunction() {
			@Override
			public LuaValue call( LuaValue a, LuaValue b ) {
				return CoerceJavaToLua.coerce( Power( toJava( a ), toJava( b ) ) );
			}
		} );
		globals.set( "pow", globals.get( "power" ) );

		globals.set( "root", new TwoArgFunction() {
			@Override
			public LuaValue call( LuaValue a, LuaValue b ) {
				return CoerceJavaToLua.coerce( 
					Root( toJava( a ), toJava( b ) )
				 );
			}
		} );

		globals.set( "abs", new OneArgFunction() {
			@Override
			public LuaValue call( LuaValue a ) {
				return CoerceJavaToLua.coerce( Abs( toJava( a ) ) );
			}
		} );
		globals.set( "min", new VarArgFunction() {
			@Override
			public Varargs invoke( Varargs args ) {
				return CoerceJavaToLua.coerce( Min( toJava( args ) ) );
			}
		} );

		globals.set( "max", new VarArgFunction() {
			@Override
			public Varargs invoke( Varargs args ) {
				return CoerceJavaToLua.coerce( Max( toJava( args ) ) );
			}
		} );
		
		globals.set( "random", new VarArgFunction() {
			@Override
			public Varargs invoke( Varargs args ) {
				int count = args.narg();

				if (count == 0) {
					return CoerceJavaToLua.coerce( Random() );
				}

				if (count == 2) {
					return CoerceJavaToLua.coerce( 
						Random( toJava( args.arg(1) ), toJava( args.arg(2) ) )
					 );
				}

				throw new IllegalArgumentException( "random expects () or (a, b)" );
			}
		} );
		globals.set("randomSign", new ZeroArgFunction() {
			@Override
			public LuaValue call() {
				return CoerceJavaToLua.coerce( RandomSign() );
			}
		});
		
		// TWEENSERVICE
		LuaTable easing = new LuaTable();

		easing.set("linear", CoerceJavaToLua.coerce(Easing.LINEAR));
		easing.set("quadIn", CoerceJavaToLua.coerce(Easing.QUAD_IN));
		easing.set("quadOut", CoerceJavaToLua.coerce(Easing.QUAD_OUT));
		easing.set("quadInOut", CoerceJavaToLua.coerce(Easing.QUAD_IN_OUT));
		
		globals.set("easing", easing);
		globals.set("tween", new VarArgFunction() {
			@Override
			public Varargs invoke(Varargs args) {
				Object[] values = new Object[args.narg()];

				for (int i = 0; i < args.narg(); i++) {
					values[i] = toJava(args.arg(i + 1));
				}

				return CoerceJavaToLua.coerce(Tween(values));
			}
		});
		
		// PRINT
		globals.set( "jsprint", new OneArgFunction() {
			@Override
			public LuaValue call( LuaValue value ) {
				return CoerceJavaToLua.coerce( LuaPrint( toJava( value ) ) );
			}
		} );
		
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