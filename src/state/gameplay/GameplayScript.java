package state.gameplay;

import action.LuaSpell;
import java.util.ArrayDeque;
import java.util.Queue;
import main.Debug;

import state.Playing;
import entity.*;
import action.Spell;
import spell.*;

public final class GameplayScript {
	private final Playing parent;
	private final Queue<GameplayEvent> events;

	private GameplayEvent currentEvent;
	private boolean started;

	public GameplayScript(Playing playing) {
		this.parent = playing;
		this.events = new ArrayDeque<>();
	}

	public void start() {
		if (started) { return; }

		started = true;
		
//		events.add(new DialogueEvent(parent, "Test"));

		eventsAddSpell(TestSpell.class);
		eventsAddSpell("Remilia");
		eventsAddSpell("Yuyuko");
		eventsAddSpell("Eirin");
		eventsAddSpell("Junko");
		eventsAddSpell("Madness20");
		eventsAddSpell(Ariya.class);
	}
	
	private void eventsAddSpell(String name) {
		Spell spell = new LuaSpell( parent.getBoss(), parent.getPlayer(), name);
		events.add( new SpellEvent( parent, spell ) );
	}

	private void eventsAddSpell(Class<? extends Spell> spellClass) {
		try {
			Spell spell = spellClass
				.getConstructor(Boss.class, Player.class)
				.newInstance(
					parent.getBoss(),
					parent.getPlayer()
				);

			events.add(new SpellEvent(parent, spell));
		} catch (ReflectiveOperationException e) {
			throw Debug.terminate( this, "Failed to create spell: " + spellClass.getName() + " and " + e );
		}
	}

	public void update() {
		while (true) {
			if (currentEvent == null) {
				currentEvent = events.poll();

				if (currentEvent == null) {
					return;
				}

				currentEvent.start();
			}

			currentEvent.update();

			if (!currentEvent.isFinished()) {
				return;
			}

			currentEvent = null;
		}
	}

	public boolean isFinished() {
		return started
			&& currentEvent == null
			&& events.isEmpty();
	}
}