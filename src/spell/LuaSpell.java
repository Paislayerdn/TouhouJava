package spell;

import org.luaj.vm2.lib.jse.CoerceJavaToLua;
import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaValue;
import action.JSL;

import resource.ResourceLoader;

import action.Spell;
import action.Action;

import entity.Boss;
import entity.Player;


public class LuaSpell extends Spell {
	private final String luaFile;
	private final Player player;
	private final LuaValue luaSpell;

	public LuaSpell(Boss boss, Player player, String file) {
		super(boss, "LLL Replication Sign \"Digitalized Pebbles\"");
		this.luaFile = file;
		this.player = player;
		Globals globals = JSL.registerJScratch();
		globals.set("boss", CoerceJavaToLua.coerce(boss));
		globals.set("player", CoerceJavaToLua.coerce(player));
		String source = ResourceLoader.lua(luaFile);
		LuaValue script = globals.load( source, luaFile+".lua" );
		this.luaSpell = script.call();
	}
	
	@Override
	public void onStart() {
		luaSpell.get("onStart").call();
	}
	
	@Override
	protected Action buildAction() {
		return (Action) luaSpell.get("buildAction").call().checkuserdata(Action.class);
	}
}