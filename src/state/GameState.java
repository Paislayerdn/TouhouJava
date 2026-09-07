package state;

import java.awt.Graphics2D;

public interface GameState {
	void update();
	void draw(Graphics2D g2);
}