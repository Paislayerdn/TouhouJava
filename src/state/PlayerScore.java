package state;

import graphics.Renderer;
import graphics.TextAlign;
import graphics.TextBox;
import main.Game;
import main.Input;
import resource.ResourceLoader;
import state.gameplay.HighScoreStore;
import state.gameplay.PlayingStats;

public final class PlayerScore extends GameState {
	private final boolean cleared;
	private final TextBox[] lines = new TextBox[8];

	public PlayerScore(Game game, boolean cleared) {
		super(game);
		this.cleared = cleared;
		PlayingStats stats = game.getPlayingStats();
		long highScore = HighScoreStore.record(stats.getScore());
		lines[0] = line(cleared ? "PLAYER SCORE" : "GAME OVER", 195, 42);
		lines[1] = line("SCORE  " + stats.getScore(), 120, 30);
		lines[2] = line("HIGH SCORE  " + highScore, 65, 25);
		lines[3] = line("GRAZE  " + stats.getGraze(), 10, 25);
		lines[4] = line("DEATHS  " + stats.getDeaths(), -45, 25);
		lines[5] = line("POWER  " + stats.getPower(), -100, 25);
		lines[6] = line("LIVES  " + stats.getLives() + "   BOMBS  " + stats.getBombs(), -155, 25);
		lines[7] = line("PRESS Z TO RETURN TO TITLE", -225, 22);
	}

	private TextBox line(String text, float y, float size) {
		TextBox box = new TextBox(text, 0, y, 840, 70, TextAlign.CENTER);
		box.setGlyphSize(size);
		return box;
	}

	@Override
	public void update() {
		if (parent.isFront(this) && Input.tap.Z) {
			parent.removeState(this);
			parent.pushState(new LoadingState(parent, LoadingState.Route.TITLE));
		}
	}

	@Override
	public void draw(Renderer renderer) {
		renderer.beginTitle();
		renderer.scale(0.5f, 0.5f);
		renderer.image(ResourceLoader.image("TSC1"), 0, 0);
		renderer.scale(2.0f, 2.0f);
		for (TextBox line : lines) line.draw(renderer);
		renderer.endTitle();
	}
}
