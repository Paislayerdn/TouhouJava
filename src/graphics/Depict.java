package graphics;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.BasicStroke;
import java.awt.geom.Point2D;
import java.awt.AlphaComposite;

import entity.Appearance;
import entity.Thing;

public final class Depict {
	// Constants
	private static final float DEFAULT_STROKE_WIDTH = 3;
	private static final float DEFAULT_LINE_WIDTH = 1;
	private static final float DEFAULT_OUTLINE_WIDTH = 1;
	
	
	// Utility class
	private Depict() {}

	public static void circle(Graphics2D g2, double x, double y, double radius) {
		oval(g2, x, y, radius, radius, 0);
	}
	public static void circle(Graphics2D g2, double x, double y, double radius, double angle) {
		oval(g2, x, y, radius, radius, angle);
	}
	public static void circleOutline(Graphics2D g2, double x, double y, double radius) {
		circleOutline(g2, x, y, radius, DEFAULT_OUTLINE_WIDTH);
	}
	public static void circleOutline(Graphics2D g2, double x, double y, double radius, float thickness) {
		var oldStroke = g2.getStroke();
		g2.setStroke(new BasicStroke(thickness));
		
		g2.draw( new Ellipse2D.Double(x-radius, y-radius, radius * 2, radius * 2) );
		g2.setStroke(oldStroke);
	}

	public static void oval(Graphics2D g2, double x, double y, double width, double height) {
		oval(g2, x, y, width, height, 0);
	}
	public static void oval(Graphics2D g2, double x, double y, double width, double height, double angle) {
		AffineTransform old = g2.getTransform();

		g2.translate(x, y);
		g2.rotate(Math.toRadians(angle));

		g2.fill( new Ellipse2D.Double(-width/2, -height/2, width, height) );
		g2.setTransform(old);
	}
	
	public static void rectangleOutline(Graphics2D g2, double x, double y, double width, double height) {
		rectangleOutline(g2, x, y, width, height, DEFAULT_OUTLINE_WIDTH);
	}
	public static void rectangleOutline(Graphics2D g2, double x, double y, double width, double height, float thickness) {
		var oldStroke = g2.getStroke();
		g2.setStroke(new BasicStroke(thickness));

		g2.drawRect((int)(x - width / 2), (int)(y - height / 2),
			(int)width, (int)height
		);

		g2.setStroke(oldStroke);
	}

	public static void line(Graphics2D g2, double x1, double y1, double x2, double y2) {
		line(g2, x1, y1, x2, y2, DEFAULT_STROKE_WIDTH);
	}
	public static void line(Graphics2D g2, double x1, double y1, double x2, double y2, float thickness) {
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
			double scale = appearance.size / 100.0;
			g2.scale(scale, scale);
		}

		if (appearance.ghost != 0) {
			float alpha = 1.0f - (float)appearance.ghost / 100.0f;

			g2.setComposite( AlphaComposite.getInstance( AlphaComposite.SRC_OVER, alpha) );
		}

		drawCenteredImage(g2, image);
		
		g2.setComposite(oldComposite);
		g2.setTransform(oldTransform);
	}
	public static void image(Graphics2D g2,
		BufferedImage image,
		double x, double y
	) {
		AffineTransform old = g2.getTransform();

		g2.translate(x, y);

		drawCenteredImage(g2, image);

		g2.setTransform(old);
	}
	
	public static void image(Graphics2D g2,
		BufferedImage image,
		double x, double y,
		double width, double height
	) {
		AffineTransform old = g2.getTransform();

		g2.translate(x, y);

		// Cancel the world's Y flip so the image is upright.
		g2.scale(1, -1);

		g2.drawImage(
			image, (int) (-width / 2), (int) (-height / 2),
			(int) width, (int) height, null
		);

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