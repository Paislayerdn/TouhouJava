package state;

import graphics.Renderer;

public interface GameState {
	void update();
	void draw(Renderer renderer);
}