package state.gameplay;

import state.Playing;

public final class DialogueEvent implements GameplayEvent {
	private final Playing playing;
	private final String dialogue;

	public DialogueEvent(Playing playing, String dialogue) {
		this.playing = playing;
		this.dialogue = dialogue;
	}

	@Override
	public void start() {
		playing.converse(dialogue);
	}

	@Override
	public void update() {
	}

	@Override
	public boolean isFinished() {
		return playing.getDialogueRunner() == null
			|| playing.getDialogueRunner().isFinished();
	}
}