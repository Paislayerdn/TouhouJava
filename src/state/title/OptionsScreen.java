package state.title;

import java.awt.image.BufferedImage;

import entity.Thing;
import graphics.Renderer;
import main.Game;
import main.Input;
import resource.ResourceLoader;
import state.GameState;

import static action.JScratch.*;

/** A lightweight placeholder until the game has real options to configure. */
public final class OptionsScreen extends GameState {
	private final BufferedImage background;
	private final Thing border;
	private final Thing logo;
	private final Thing placeholder;

	public OptionsScreen(Game game) {
		super(game);
		background = ResourceLoader.image("TSC1");
		border = new Thing(0, 0, "TSC4", 100);
		border.run(Sequence(
			Var("t", 0),
			Forever("Sequence",
				SetBrightness(Add(5, Mul(5, Sin(Get("t"))))),
				Set("t", Mod(Add(Get("t"), 1), 360)),
				Wait()
			)
		));
		logo = new Thing(0, 0, "TSC5", 100);
		placeholder = new Thing(0, 0, "BulletDonut", 300);
	}

	@Override
	public void update() {
		logo.update();
		placeholder.update();
		border.update();
		if (parent.isFront(this) && Input.tap.X) finish();
	}

	@Override
	public void draw(Renderer renderer) {
		renderer.beginTitle();
		renderer.scale(0.5f, 0.5f);
		renderer.image(background, 0, 0);
		logo.draw(renderer);
		placeholder.draw(renderer);
		border.draw(renderer);
		renderer.endTitle();
	}
}
