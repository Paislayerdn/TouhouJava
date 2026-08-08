package action;

import entity.Boss;

public abstract class Spell extends Action {
	protected Boss boss;
	protected Action action;
	protected final String name;
	
	public Spell(Boss boss) {
		this.boss = boss;
		this.name = "[SPELL UNNAMED]";
	}
	public Spell(Boss boss, String name) {
		this.boss = boss;
		this.name = name;
	}

	public String getName() { return name; }

	public void onStart() {}
	public void onEnd() {}

	protected abstract Action buildAction();

	@Override
	public void start() {
		action = buildAction();
		action.setOwner(boss);
		action.setContext(getContext());
		action.start();
		onStart();
	}

	@Override
	public void update() {
		if (action == null || finished)
			return;

		if (!action.isFinished()) {
			action.update();
		}

		if (action.isFinished()) {
			onEnd();
			finish();
		}
	}

	@Override
	public void reset() {
		super.reset();
		action = null;
	}
}