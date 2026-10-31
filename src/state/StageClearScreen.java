package state;

import graphics.Renderer;
import graphics.TextAlign;
import graphics.TextBox;
import main.Game;
import main.Input;
import state.gameplay.ScoreRules;

public final class StageClearScreen extends GameState {
	private final TextBox title = new TextBox("STAGE CLEAR", 0, 50, 720, 100, TextAlign.CENTER);
	private final TextBox bonus = new TextBox(
		"CLEAR BONUS  +" + ScoreRules.STAGE_CLEAR_BONUS,
		0, -35, 840, 80, TextAlign.CENTER);
	private final TextBox prompt = new TextBox("PRESS Z", 0, -140, 600, 80, TextAlign.CENTER);

	public StageClearScreen(Game game) {
		super(game);
		title.setGlyphSize(52);
		bonus.setGlyphSize(28);
		prompt.setGlyphSize(28);
	}

	@Override
	public void update() {
		if (parent.isFront(this) && Input.tap.Z) {
			parent.removeState(this);
			parent.pushState(new CreditsScreen(parent));
		}
	}

	@Override
	public void draw(Renderer renderer) {
		renderer.beginTitle();
		title.draw(renderer);
		bonus.draw(renderer);
		prompt.draw(renderer);
		renderer.endTitle();
	}
}
