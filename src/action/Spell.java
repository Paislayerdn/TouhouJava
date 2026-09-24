package action;

import entity.Boss;
import entity.Player;
import resource.ResourceLoader;
import resource.Sound;

public abstract class Spell extends Action {
	protected final Boss boss;
	protected final Player player;
	protected Action action;
	@Override
	public boolean consumesFrame() { return false; }
	
	protected String name = "[SPELL UNNAMED]";
	protected float playerCandidateRadius = 50.0f;
	protected boolean isSpell = false;
	protected String caster = null;

	public final boolean isSpell() { return isSpell; }
	public final String getCaster() { return caster; }
	
	protected float timer = 1770.0f; // 29.5 seconds
	protected float countableTime;
	private boolean timerStarted;
	private boolean counting;
	private int lastSecond;
	private final Sound LS;
	public final float getTimer() { return timer; }
	public final void startTimer() {
		lastSecond = (int) Math.ceil(timer / 60.0f);
		timerStarted = true;
	}
	public final void startCounting() { counting = true; } 
	public final boolean isCounting() { return counting; }
	public final float getProgression() { return ((counting)? (1.0f - timer / countableTime) : 0.0f ); }
	
	public Spell(Boss boss, Player player) {
		this.boss = boss;
		this.player = player;
		this.LS = ResourceLoader.sound("[TH] Timer");
		this.LS.setVolume(0.0f);
	}

	public void onEnd() {}

	protected abstract void configure();
	protected abstract void onStart();
	protected abstract Action buildAction();

	@Override
	public final void start() {
		action = buildAction();
		action.setContext(getContext());
		action.start();
		onStart();
	}

	@Override
	public void update() {
		if (timerStarted && timer > 0.0f) {
			timer--;

			int second = (int) Math.ceil(timer / 60.0f);

			if (second != lastSecond && second >= 1 && second <= 10) {
				LS.play();
				lastSecond = second;
			}
		}

		if (counting) {
			// progression advances
			// score countdown advances
		}

		if (action == null || finished) return;

		if (!action.isFinished()) {
			action.update();
		}

		if (boss.getHP() <= 0.0f || timer <= 0.0f || action.isFinished()) {
			onEnd();
			finish();
		}
	}

	@Override
	public final void reset() {
		super.reset();
		action = null;
	}
}