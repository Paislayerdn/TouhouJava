package dialogue;

public final class DialogueMovement {
	private final DialogueSpeaker speaker;

	private final float startX;
	private final float startY;
	private final float targetX;
	private final float targetY;
	private final int duration;

	private int elapsed;

	public DialogueMovement(
		DialogueSpeaker speaker,
		float targetX,
		float targetY,
		int duration
	) {
		this.speaker = speaker;

		startX = speaker.getX();
		startY = speaker.getY();

		this.targetX = targetX;
		this.targetY = targetY;
		this.duration = duration;
	}

	public void update() {
		if (duration <= 0) {
			speaker.setPosition(targetX, targetY);
			return;
		}

		elapsed++;

		float progress = Math.min(elapsed / duration, 1.0f);

		float x = startX + (targetX - startX) * progress;
		float y = startY + (targetY - startY) * progress;

		speaker.setPosition(x, y);
	}

	public boolean isFinished() {
		return duration <= 0 || elapsed >= duration;
	}
}