package action;

import entity.Boss;
import entity.Player;

public abstract class Spell extends Action {
	protected final Boss boss;
	protected final Player player;
	protected Action action;
	
	protected String name = "[SPELL UNNAMED]";
	protected float playerCandidateRadius = 50.0f;
	protected float timer = 3600;
	protected boolean isSpell = false;
	
	public Spell(Boss boss, Player player) {
		this.boss = boss;
		this.player = player;
	}

	public void onEnd() {}

	protected abstract void configure();
	protected abstract void onStart();
	protected abstract Action buildAction();

	@Override
	public void start() {
		action = buildAction();
//		action.setOwner(boss);
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

		if (boss.getHP() <= 0.0f || action.isFinished()) {
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