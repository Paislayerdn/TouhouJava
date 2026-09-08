package main;

import java.awt.Graphics2D;

import state.gameplay.PlayingStats;
import state.GameState;
import state.Playing;
import state.TitleScreen;

public class Game {
	private GameState currentState;
	private PlayingStats playingStats;

	public Game() {
		currentState = new TitleScreen();
	}
	
	public void update() {
		currentState.update();
	}

	public void draw(Graphics2D g2) {
		currentState.draw(g2);
	}
	
	public void setPlayingStats(PlayingStats playingStats) { this.playingStats = playingStats; }
	public PlayingStats getPlayingStats() { return playingStats; }
}