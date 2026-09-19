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


public final class LuaSpell extends Spell {
	private final String luaFile;
	private final LuaValue luaSpell;

	public LuaSpell(Boss boss, Player player, String file) {
		super(boss, player);
		this.luaFile = file;
		name = "[UNNAMED LUASPELL]";
		 
		Globals globals = JSL.registerJScratch();
		globals.set("boss", CoerceJavaToLua.coerce(boss));
		globals.set("player", CoerceJavaToLua.coerce(player));
		String source = ResourceLoader.lua(luaFile);
		LuaValue script = globals.load( source, luaFile+".lua" );
		this.luaSpell = script.call();
	
		configure();
	}
	
	@Override
	protected void configure() {
		LuaValue config = luaSpell.get("config");

		if (config.isnil()) return;

		LuaValue value;
		value = config.get("name");
		if (!value.isnil()) name = value.tojstring();
		
		value = config.get("playerCandidateRadius");
		if (!value.isnil()) playerCandidateRadius = value.tofloat();

		value = config.get("timer");
		if (!value.isnil()) timer = value.tofloat();

		value = config.get("isSpell");
		if (!value.isnil()) isSpell = value.toboolean();
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