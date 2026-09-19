package graphics;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.BasicStroke;
import java.awt.AlphaComposite;

import entity.Appearance;
import entity.Thing;
import java.awt.Color;

public final class Depict {
	// Constants
	private static final float DEFAULT_STROKE_WIDTH = 3;
	private static final float DEFAULT_LINE_WIDTH = 1;
	private static final float DEFAULT_OUTLINE_WIDTH = 1;
	
	
	// Utility class
	private Depict() {}

	public static void circle(Graphics2D g2, float x, float y, float radius) {
		oval(g2, x, y, radius, radius, 0);
	}
	public static void circle(Graphics2D g2, float x, float y, float radius, float angle) {
		oval(g2, x, y, radius, radius, angle);
	}
	public static void circleOutline(Graphics2D g2, float x, float y, float radius) {
		circleOutline(g2, x, y, radius, DEFAULT_OUTLINE_WIDTH);
	}
	public static void circleOutline(Graphics2D g2, float x, float y, float radius, float thickness) {
		var oldStroke = g2.getStroke();
		g2.setColor(Color.RED);
		g2.setStroke(new BasicStroke(thickness));
		
		g2.draw( new Ellipse2D.Float(x-radius, y-radius, radius * 2, radius * 2) );
		g2.setStroke(oldStroke);
	}

	public static void oval(Graphics2D g2, float x, float y, float width, float height) {
		oval(g2, x, y, width, height, 0);
	}
	public static void oval(Graphics2D g2, float x, float y, float width, float height, float angle) {
		AffineTransform old = g2.getTransform();

		g2.translate(x, y);
		g2.rotate(Math.toRadians(angle));
		g2.setColor(Color.RED);

		g2.fill( new Ellipse2D.Float(-width/2, -height/2, width, height) );
		g2.setTransform(old);
	}
	
	public static void rectangleOutline(Graphics2D g2, float x, float y, float width, float height) {
		rectangleOutline(g2, x, y, width, height, DEFAULT_OUTLINE_WIDTH);
	}
	public static void rectangleOutline(Graphics2D g2, float x, float y, float width, float height, float thickness) {
		var oldStroke = g2.getStroke();
		g2.setColor(Color.RED);
		g2.setStroke(new BasicStroke(thickness));

		g2.drawRect((int)(x - width / 2), (int)(y - height / 2),
			(int)width, (int)height
		);

		g2.setStroke(oldStroke);
	}

	public static void line(Graphics2D g2, float x1, float y1, float x2, float y2) {
		line(g2, x1, y1, x2, y2, DEFAULT_STROKE_WIDTH);
	}
	public static void line(Graphics2D g2, float x1, float y1, float x2, float y2, float thickness) {
		var oldStroke = g2.getStroke();
		g2.setStroke( new BasicStroke(thickness) );

		g2.drawLine( (int)x1, (int)y1, (int)x2, (int)y2 );
		g2.setStroke(oldStroke);
	}


	public static void thing(Graphics2D g2, Thing thing) {
		Appearance appearance = thing.getAppearance();

		if (appearance.costume == null
				|| appearance.size == 0
				|| appearance.ghost == 100) {
			return;
		}

		BufferedImage image = appearance.getRenderedCostume();

		AffineTransform oldTransform = g2.getTransform();
		var oldComposite = g2.getComposite();

		g2.translate(thing.getX(), thing.getY());
		g2.rotate(Math.toRadians(thing.getTrueAngle()));

		if (appearance.size != 100) {
			float scale = appearance.size / 100.0f;
			g2.scale(scale, scale);
		}

		if (appearance.ghost != 0) {
			float alpha = 1.0f - appearance.ghost / 100.0f;

			g2.setComposite( AlphaComposite.getInstance( AlphaComposite.SRC_OVER, alpha) );
		}

		drawCenteredImage(g2, image);
		
		g2.setComposite(oldComposite);
		g2.setTransform(oldTransform);
	}
	public static void image(Graphics2D g2,
		BufferedImage image,
		float x, float y
	) {
		AffineTransform old = g2.getTransform();

		g2.translate(x, y);

		drawCenteredImage(g2, image);

		g2.setTransform(old);
	}
	
	public static void image(Graphics2D g2,
		BufferedImage image,
		float x, float y,
		float width, float height
	) {
		AffineTransform old = g2.getTransform();
		g2.translate(x, y);

		float halfWidth = width / 2.0f;
		float halfHeight = height / 2.0f;

		// Cancel the world's Y flip so the image is upright.
		g2.scale(1, -1);

		g2.drawImage(image, (int) -halfWidth, (int) -halfHeight, (int) width, (int) height, null);

		g2.setTransform(old);
	}
	
	private static void drawCenteredImage(Graphics2D g2, BufferedImage image) {
		// Cancel the world's Y flip so the image is upright.
		g2.scale(1, -1);
		g2.drawImage(
			image, -image.getWidth() / 2, -image.getHeight() / 2, null
		);
	}
}