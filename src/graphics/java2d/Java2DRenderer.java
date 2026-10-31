package graphics.java2d;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.geom.AffineTransform;
import java.awt.geom.Arc2D;
import java.awt.BasicStroke;
import java.awt.Color;
import graphics.Renderer;

import main.Settings;
import entity.Thing;
import background3d.BackgroundCamera;
import background3d.BackgroundObject;

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
	public void beginBackground(BackgroundCamera camera) {
	}

	@Override
	public void backgroundObject(BackgroundObject object) {
	}

	@Override
	public boolean usesBackgroundFallback() {
		return true;
	}

	@Override
	public void backgroundFallback(BufferedImage image) {
		Depict.image(g2, image,
			-Settings.PLAYFIELD_CENTER_X,
			-Settings.PLAYFIELD_CENTER_Y,
			Settings.BASE_WIDTH,
			Settings.BASE_HEIGHT);
	}

	@Override
	public void endBackground() {
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
	public void circleArc(float x, float y, float radius, float startAngle, float extentAngle,
		float red, float green, float blue, float width) {
		var oldStroke = g2.getStroke();
		var oldColor = g2.getColor();
		g2.setColor(new Color(clampColor(red), clampColor(green), clampColor(blue)));
		g2.setStroke(new BasicStroke(width, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
		g2.draw(new Arc2D.Float(x - radius, y - radius, radius * 2, radius * 2,
			startAngle, extentAngle, Arc2D.OPEN));
		g2.setStroke(oldStroke);
		g2.setColor(oldColor);
	}

	private static float clampColor(float value) {
		return Math.max(0, Math.min(1, value));
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
}
