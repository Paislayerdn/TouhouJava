package graphics;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.BasicStroke;

public final class Depict {
	// Constants
	private static final float DEFAULT_LINE_WIDTH = 1;
	
	
	private Depict() {
		// Utility class
	}

	public static void circle(Graphics2D g2, double x, double y, double radius) {
		oval(g2, x, y, radius, radius);
	}


	public static void oval(Graphics2D g2, double x, double y, double width, double height) {
		g2.fill( new Ellipse2D.Double( x - width / 2, y - height / 2, width, height) );
	}

	// due to Java's limitation method signatures must be exact, you cannot put default value to arguments
	public static void line(Graphics2D g2, double x1, double y1, double x2, double y2) {
		g2.draw( new Line2D.Double( x1, y1, x2, y2 ) );
	}
	public static void line(Graphics2D g2, double x1, double y1, double x2, double y2, float thickness) {
		var oldStroke = g2.getStroke();

		g2.setStroke( new BasicStroke(thickness) );

		g2.drawLine( (int)x1, (int)y1, (int)x2, (int)y2 );

		g2.setStroke(oldStroke);
}


	public static void image( Graphics2D g2, BufferedImage image, double x, double y) {
		AffineTransform old = g2.getTransform();

		AffineTransform at = new AffineTransform();
		at.translate(x - image.getWidth()/2.0, -y - image.getHeight()/2.0);
		g2.scale(1, -1);

		g2.drawImage(image, at,null);
		g2.setTransform(old);
	}
}