package dialogue;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import graphics.Depict;
import resource.ResourceLoader;

public final class DialogueRenderer {
	private DialogueRenderer() {}

	public static void draw(Graphics2D g2, DialogueRunner runner) {
		for (DialogueSpeaker speaker : runner.getDialogue().getAllSpeakers()) {
			String expression = speaker.getExpression();

			if (expression == null) continue;

			BufferedImage image = ResourceLoader.image(expression);

			Depict.dialogueImage(g2, image, speaker.getX(), speaker.getY());
		}
	}
}