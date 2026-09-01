package dialogue;

public class DialogueMovement {
	private final DialogueSpeaker speaker;

	private final double startX;
	private final double startY;
	private final double targetX;
	private final double targetY;
	private final int duration;

	private int elapsed;

	public DialogueMovement(
		DialogueSpeaker speaker,
		double targetX,
		double targetY,
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

		double progress =
			Math.min((double) elapsed / duration, 1.0);

		double x =
			startX + (targetX - startX) * progress;

		double y =
			startY + (targetY - startY) * progress;

		speaker.setPosition(x, y);
	}

	public boolean isFinished() {
		return duration <= 0 || elapsed >= duration;
	}
}