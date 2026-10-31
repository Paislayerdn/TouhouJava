package action;

import entity.Boss;
import entity.Player;
import resource.ResourceLoader;
import resource.Sound;

public abstract class Spell extends Action {
	protected final Boss boss;
	protected final Player player;
	protected Action action;
	private ActionContext spellContext;
	@Override
	public boolean consumesFrame() { return false; }

	protected String name = "[SPELL UNNAMED]";
	protected float playerCandidateRadius = 50.0f;
	protected boolean isSpell = false;
	protected boolean survival = false;
	protected String caster = null;
	protected long scoreReward = 10_000_000L;

	protected float timer = 1770.0f; // 29.5 seconds
	private float startingTimer;
	private boolean timerStarted;
	private int lastSecond;
	private int deathsAtStart;
	private long scoreAwarded;
	private boolean captured;
	private boolean failed;
	private final Sound LS;

	public final void startTimer() {
		lastSecond = (int) Math.ceil(timer / 60.0f);
		startingTimer = timer;
		timerStarted = true;
	}

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
		deathsAtStart = player.getStats().getDeaths();
		spellContext = new ActionContext(getContext());

		action = buildAction();
		action.setContext(spellContext);
		action.start();
		onStart();
	}

	@Override
	public void update() {
		if (finished) return;

		if (timerStarted && timer > 0.0f) {
			timer--;

			int second = (int) Math.ceil(timer / 60.0f);

			if (second != lastSecond && second >= 1 && second <= 10) {
				LS.play();
				lastSecond = second;
			}
		}

		if (timerStarted && timer <= 0.0f) {
			capture();
			return;
		}
		if (caster != null && !survival && boss.getHP() <= 0.0f) {
			capture();
			return;
		}
		if (action == null) return;

		if (!action.isFinished()) {
			action.update();
		}

		if (action.isFinished()) {
			capture();
		}
	}

	public final void end() {
		if (finished) return;
		capture();
	}

	private void capture() {
		if (finished) return;
		captured = true;
		failed = isBonusFailed();
		boolean bossHPDepleted = caster != null && !survival && boss.getHP() <= 0;

		onEnd();
		if (isSpell && !failed) {
			scoreAwarded = getCurrentBonus();
		}
		if (bossHPDepleted && (!isSpell || !failed)) {
			scoreAwarded += state.gameplay.ScoreRules.BOSS_HP_DEPLETION_REWARD;
		}
		player.getStats().addScore(scoreAwarded);
		finish();
	}

	@Override
	public final void reset() {
		super.reset();
		action = null;
	}
	
	public String getName() { return name; }
	public float getPlayerCandidateRadius() { return playerCandidateRadius; }
	public final boolean isSpell() { return isSpell; }
	public final boolean isSurvival() { return survival; }
	public boolean isTimerStarted() { return timerStarted; }
	public int getLastSecond() { return lastSecond; }
	public final String getCaster() { return caster; }
	public final float getTimer() { return timer; }
	public final long getScoreReward() { return scoreReward; }
	public final long getCurrentBonus() {
		if (isBonusFailed()) return 0;
		double value = 1.0;
		if (!survival && timerStarted && startingTimer > 0) {
			double remaining = Math.max(0, Math.min(1, timer / startingTimer));
			value = 0.1 + 0.9 * remaining;
		}
		return Math.round(scoreReward * value);
	}
	public final boolean isBonusFailed() {
		return player.getStats().getDeaths() > deathsAtStart;
	}
	public final long getScoreAwarded() { return scoreAwarded; }
	public final boolean wasCaptured() { return captured; }
	public final boolean isFailed() {
		return failed;
	}
	public final float getElapsedSeconds() {
		return Math.max(0, startingTimer - timer) / 60.0f;
	}
}
