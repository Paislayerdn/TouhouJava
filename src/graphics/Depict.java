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

		BufferedImage image = appearance.costume;

		// Expensive image effects
		if (appearance.pixelate != 0) {
			image = pixelate(image, appearance.pixelate);
		}

		if (appearance.color != 0) {
			image = color(image, appearance.color);
		}

		if (appearance.brightness != 0) {
			image = brightness(image, appearance.brightness);
		}

		AffineTransform oldTransform = g2.getTransform();
		var oldComposite = g2.getComposite();

		g2.translate(thing.getX(), -thing.getY());
		g2.rotate(Math.toRadians(thing.getTrueAngle()));

		if (appearance.size != 100) {
			double scale = appearance.size / 100.0;
			g2.scale(scale, scale);
		}

		if (appearance.ghost != 0) {
			float alpha = 1.0f - appearance.ghost / 100.0f;
			g2.setComposite(
				AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha)
			);
		}

		g2.drawImage(image, -image.getWidth() / 2, -image.getHeight() / 2, null);

		g2.setComposite(oldComposite);
		g2.setTransform(oldTransform);
	}
	private static BufferedImage pixelate(BufferedImage source, int amount) {
		if (amount <= 0) {
			return source;
		}

		int blockSize = amount + 1;

		BufferedImage result = new BufferedImage(
			source.getWidth(),
			source.getHeight(),
			BufferedImage.TYPE_INT_ARGB
		);

		for (int y = 0; y < source.getHeight(); y += blockSize) {
			for (int x = 0; x < source.getWidth(); x += blockSize) {

				int sample = source.getRGB(x, y);

				int maxX = Math.min(x + blockSize, source.getWidth());
				int maxY = Math.min(y + blockSize, source.getHeight());

				for (int py = y; py < maxY; py++) {
					for (int px = x; px < maxX; px++) {
						result.setRGB(px, py, sample);
					}
				}
			}
		}

		return result;
	}
	private static BufferedImage color(BufferedImage source, int amount) {
		if (amount == 0) {
			return source;
		}

		BufferedImage result = new BufferedImage(
			source.getWidth(),
			source.getHeight(),
			BufferedImage.TYPE_INT_ARGB
		);

		float hueShift = amount / 256.0f;

		for (int y = 0; y < source.getHeight(); y++) {
			for (int x = 0; x < source.getWidth(); x++) {
				int argb = source.getRGB(x, y);

				int alpha = (argb >>> 24) & 0xFF;

				// Fully transparent: leave it alone.
				if (alpha == 0) {
					result.setRGB(x, y, argb);
					continue;
				}

				int red   = (argb >>> 16) & 0xFF;
				int green = (argb >>> 8) & 0xFF;
				int blue  = argb & 0xFF;

				float[] hsb = java.awt.Color.RGBtoHSB(
					red, green, blue, null
				);

				// Grayscale has no hue, so leave it unchanged.
				if (hsb[1] != 0) {
					hsb[0] = (hsb[0] + hueShift) % 1.0f;
				}

				int rgb = java.awt.Color.HSBtoRGB(
					hsb[0], hsb[1], hsb[2]
				);

				result.setRGB(
					x, y,
					(alpha << 24) | (rgb & 0x00FFFFFF)
				);
			}
		}

		return result;
	}
	private static BufferedImage brightness(BufferedImage source, int amount) {
		if (amount == 0) {
			return source;
		}

		BufferedImage result = new BufferedImage(
			source.getWidth(),
			source.getHeight(),
			BufferedImage.TYPE_INT_ARGB
		);

		double factor = Math.abs(amount) / 100.0;

		for (int y = 0; y < source.getHeight(); y++) {
			for (int x = 0; x < source.getWidth(); x++) {
				int argb = source.getRGB(x, y);

				int alpha = (argb >>> 24) & 0xFF;

				if (alpha == 0) {
					result.setRGB(x, y, argb);
					continue;
				}

				int red   = (argb >>> 16) & 0xFF;
				int green = (argb >>> 8) & 0xFF;
				int blue  = argb & 0xFF;

				if (amount < 0) {
					// Move toward black.
					red   = (int) (red   * (1.0 - factor));
					green = (int) (green * (1.0 - factor));
					blue  = (int) (blue  * (1.0 - factor));
				} else {
					// Move toward white.
					red   = (int) (red   + (255 - red) * factor);
					green = (int) (green + (255 - green) * factor);
					blue  = (int) (blue  + (255 - blue) * factor);
				}

				result.setRGB(
					x, y,
					(alpha << 24)
					| (red << 16)
					| (green << 8)
					| blue
				);
			}
		}

		return result;
	}
	
	
	public static void worldImage( Graphics2D g2, BufferedImage image, double x, double y) {
		AffineTransform old = g2.getTransform();

		AffineTransform at = new AffineTransform();
		at.translate(x - image.getWidth()/2.0, -y - image.getHeight()/2.0);
		g2.scale(1, -1);
		g2.drawImage(image, at,null);
		
		g2.setTransform(old);
	}
	
	public static void dialogueImage(Graphics2D g2, BufferedImage image, double x, double y) {
		Point2D.Double screen = DialogueCoordinate.toScreen(x, y);

		g2.drawImage(image,
			(int)(screen.x - image.getWidth() / 2.0),
			(int)(screen.y - image.getHeight() / 2.0),
			null
		);
	}
	
	public static void image(Graphics2D g2, BufferedImage image) {
		g2.drawImage(image, -image.getWidth() / 2, -image.getHeight() / 2, null);
	}
}