package action;

import org.luaj.vm2.LuaValue;
import main.Debug;

public final class LuaCallAction extends Action {
	private final LuaValue table;
	private final Object index;

	private Action action;

	LuaCallAction(LuaValue table, Object index) {
		this.table = table;
		this.index = index;
	}

	@Override
	public boolean consumesFrame() {
		return action != null && action.consumesFrame();
	}

	@Override
	public void start() {
		Object resolvedIndex = resolve(index);

		if (!(resolvedIndex instanceof Number)) {
			throw Debug.terminate("JSL", this,
				"Lua table index must resolve to a number: " + resolvedIndex
			);
		}

		int i = ((Number) resolvedIndex).intValue();

		LuaValue function = table.get(i);
		function.checkfunction();

		LuaValue result = function.call();
		action = (Action) result.checkuserdata(Action.class);

		action.setOwner(owner);
		action.setContext(context);
		action.start();
	}

	@Override
	public void update() {
		if (action.isFinished()) {
			finish();
			return;
		}

		action.update();

		if (action.isFinished()) {
			finish();
		}
	}
}