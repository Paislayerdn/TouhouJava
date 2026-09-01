package spell;

import org.luaj.vm2.lib.jse.CoerceJavaToLua;
import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaValue;
import jsl.JSL;

import resource.ResourceLoader;

import action.Spell;
import action.Action;

import entity.Boss;
import entity.Player;


public class LuaSpell extends Spell {
	private final String luaFile;
	private final Player player;

	public LuaSpell(Boss boss, Player player, String file) {
		super(boss, "LLL Replication Sign \"Digitalized Pebbles\"");
		this.luaFile = file;
		this.player = player;
	}
	
	@Override
	public void onStart() {
		boss.setMaxHP(50);
	}

	@Override
	protected Action buildAction() {
		Globals globals = JSL.registerJScratch();
		globals.set("boss", CoerceJavaToLua.coerce(boss));
		globals.set("player", CoerceJavaToLua.coerce(player));
		
		String source = ResourceLoader.lua(luaFile);

		LuaValue script = globals.load( source, luaFile+".lua" );
		LuaValue result = script.call();

		return (Action) result.checkuserdata(Action.class);
	}
}