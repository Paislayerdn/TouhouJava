package state;

import graphics.Renderer;
import main.Game;

public abstract class GameState {
	protected final Game parent;
	private boolean finished;
	protected GameState(Game parent) { this.parent = parent; }
	public abstract void update();
	public abstract void draw(Renderer renderer);
	public final boolean isFinished() { return finished; }
	protected final void finish() { finished = true; }
}
