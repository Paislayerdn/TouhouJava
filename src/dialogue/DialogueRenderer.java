package dialogue;

import java.awt.image.BufferedImage;

import graphics.Renderer;
import resource.ResourceLoader;

public final class DialogueRenderer {
	private DialogueRenderer() {}

	public static void draw(Renderer renderer, DialogueRunner runner) {
		for (DialogueThing speaker : runner.getDialogue().getSpeakers()) {
			speaker.draw(renderer);
		}
	}
}