package graphics;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.geom.AffineTransform;
import main.Settings;
import entity.Thing;

public class Java2DRenderer implements Renderer {
	private AffineTransform oldTransform;
	private final Graphics2D g2;

	public Java2DRenderer(Graphics2D g2) {
		this.g2 = g2;
	}

	@Override
	public void clear() {
		// Java2D's panel already clears the background.
	}

	@Override
	public void scale(float x, float y) {
		g2.scale(x, y);
	}
	@Override
	public void thing(Thing thing) {
		Depict.thing(g2, thing);
	}

	@Override
	public void image(BufferedImage image, float x, float y) {
		Depict.image(g2, image, x, y);
	}
	@Override
	public void image(BufferedImage image,
		float x,	float y,
		float width,	float height
	) {
		Depict.image(g2, image, x, y, width, height);
	}

	@Override
	public void rectangleOutline(
		float x, float y,
		float width, float height
	) {
		Depict.rectangleOutline(g2, x, y, width, height);
	}

	@Override
	public void circleOutline(
		float x, float y,
		float radius
	) {
		Depict.circleOutline(g2, x, y, radius);
	}

	@Override
	public void beginPlayfield() {
		oldTransform = g2.getTransform();

		g2.translate(
			Settings.PLAYFIELD_CENTER_X,
			Settings.PLAYFIELD_CENTER_Y
		);
	}

	@Override
	public void endPlayfield() {
		g2.setTransform(oldTransform);
	}

	@Override
	public void beginTitle() {
		oldTransform = g2.getTransform();
	}

	@Override
	public void endTitle() {
		g2.setTransform(oldTransform);
	}

	@Override
	public void text(String text, float x, float y) {
		text(text, x, y, 18, TextAlign.LEFT);
	}

	@Override
	public void text(String text, float x, float y, int size) {
		text(text, x, y, size, TextAlign.LEFT);
	}

	@Override
	public void text(
		String text,
		float x,
		float y,
		int size,
		TextAlign align
	) {
		TextDrawer.draw(g2, text, x, y, size, align);
	}
}