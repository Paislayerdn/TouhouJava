package state;

import graphics.Renderer;
import graphics.TextAlign;
import graphics.TextBox;
import java.awt.image.BufferedImage;
import main.Game;
import main.Input;
import resource.ResourceLoader;

/** A short image slideshow before the final run summary. */
public final class CreditsScreen extends GameState {
	private static final int SLIDE_FRAMES = 240;
	private final BufferedImage[] slides = {
		ResourceLoader.image("TSC1"),
		ResourceLoader.image("TSC2F"),
		ResourceLoader.image("TSC3F")
	};
	private final TextBox heading = new TextBox("CREDITS", 0, 120, 760, 90, TextAlign.CENTER);
	private final TextBox caption = new TextBox("TOUHOU JaVA DEMO v0.1", 0, 25, 760, 80, TextAlign.CENTER);
	private final TextBox prompt = new TextBox("PRESS Z TO CONTINUE", 0, -145, 760, 70, TextAlign.CENTER);
	private int slide;
	private int slideFrame;

	public CreditsScreen(Game game) {
		super(game);
		heading.setGlyphSize(42);
		caption.setGlyphSize(30);
		prompt.setGlyphSize(22);
	}

	@Override
	public void update() {
		if (!parent.isFront(this)) return;
		if (Input.tap.Z || ++slideFrame >= SLIDE_FRAMES) {
			slideFrame = 0;
			if (++slide >= slides.length) {
				parent.removeState(this);
				parent.pushState(new PlayerScore(parent, true));
			}
		}
	}

	@Override
	public void draw(Renderer renderer) {
		renderer.beginTitle();
		renderer.scale(0.5f, 0.5f);
		renderer.image(slides[slide], 0, 0);
		renderer.scale(2.0f, 2.0f);
		heading.draw(renderer);
		caption.draw(renderer);
		prompt.draw(renderer);
		renderer.endTitle();
	}
}
