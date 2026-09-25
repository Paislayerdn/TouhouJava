package state.gameplay;

import state.Playing;
import spell.*;
import dialogue.*;

public final class GameplayScript {
	private final Playing parent;
	
	public GameplayScript(Playing playing) {
		this.parent = playing;
	}

	public void start() {
//		parent.run( new Phyllotaxis( parent.getBoss(), parent.getPlayer() ) );
//		parent.run( new LuaSpell( parent.getBoss(), parent.getPlayer(), "Madness20") );
		parent.converse("Test_1");
	}
}	