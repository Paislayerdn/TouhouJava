package state.title;

import entity.Thing;
import graphics.Depict;
import java.awt.Graphics2D;

public class TitleThing extends Thing {

	@Override
	public void update() {
		updateActions();
	}
}