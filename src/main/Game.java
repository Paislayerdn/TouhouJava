package main;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import graphics.Renderer;

import state.*;
import state.gameplay.PlayingStats;

public class Game {
	private final Deque<GameState> states = new ArrayDeque<>();
	private PlayingStats playingStats;

	public Game() {
		pushState(new Playing(this));
//		pushState(new TitleScreen(this));
//		pushState(new LoadingState(this, LoadingState.Route.TITLE));
	}

	public void pushState(GameState state) {
		states.addLast(state);
	}

	public void pushBehindFront(GameState state) {
		GameState front = states.removeLast();
		states.addLast(state);
		states.addLast(front);
	}

	public void removeState(GameState state) {
		states.remove(state);
	}

	public boolean isFront(GameState state) {
		return states.peekLast() == state;
	}

	private GameState getSecondFromFront() {
		if (states.size() < 2) return null;

		ArrayList<GameState> list = new ArrayList<>(states);
		return list.get(list.size() - 2);
	}
	public void update() {
		GameState front = states.peekLast();
		if (front != null) {
			front.update();
		}

		if (states.size() >= 2) {
			GameState behind = getSecondFromFront();
			if (behind != null) {
				behind.update();
			}
		}

		while (!states.isEmpty() && states.peekLast().isFinished()) {
			states.removeLast();
		}
	}

	public void draw(Renderer renderer) {
		if (states.size() >= 2) {
			getSecondFromFront().draw(renderer);
		}

		GameState front = states.peekLast();
		if (front != null) {
			front.draw(renderer);
		}
	}
	
	
	public void setPlayingStats(PlayingStats playingStats) { this.playingStats = playingStats; }
	public PlayingStats getPlayingStats() { return playingStats; }
}
