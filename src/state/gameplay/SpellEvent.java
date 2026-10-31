package state.gameplay;

import action.Spell;
import state.Playing;

public final class SpellEvent implements GameplayEvent {
	private final Playing playing;
	private final Spell spell;
	private boolean completionHandled;

	public SpellEvent(Playing playing, Spell spell) {
		this.playing = playing;
		this.spell = spell;
	}

	@Override
	public void start() {
		playing.run(spell);
	}

	@Override
	public void update() {
		if (spell.isFinished() && !completionHandled) {
			playing.completeEncounter(spell);
			completionHandled = true;
		}
	}

	@Override
	public boolean isFinished() {
		return spell.isFinished() && completionHandled
			&& !playing.isShowingSpellResult();
	}
}
