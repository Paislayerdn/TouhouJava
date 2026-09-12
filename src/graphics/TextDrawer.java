package graphics;

import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.Font;
import java.awt.Paint;
import java.awt.GradientPaint;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;

public final class TextDrawer {
	private static final Font DEFAULT_FONT = new Font("Arial", Font.PLAIN, 18);

	// Utility class
	private TextDrawer() {}

	public static void draw(Graphics2D g2, String text, double x, double y) {
		draw(g2, text, x, y, DEFAULT_FONT);
	}

	public static void draw(Graphics2D g2, String text, double x, double y, Font font) {
		Font oldFont = g2.getFont();
		Paint oldPaint = g2.getPaint();
		AffineTransform oldTransform = g2.getTransform();

		g2.setRenderingHint(
			RenderingHints.KEY_TEXT_ANTIALIASING,
			RenderingHints.VALUE_TEXT_ANTIALIAS_ON
		);

		g2.setFont(font);

		// Move to the game's Cartesian coordinate.
		g2.translate(x, y);

		// Cancel the global Y flip so text is readable.
		g2.scale(1, -1);

		g2.setPaint(new GradientPaint(
			0,
			-font.getSize(),
			Color.WHITE,
			0,
			0,
			new Color(80, 170, 255)
		));

		/*
		 * x = 0 because we already translated.
		 *
		 * y = 0 is the baseline.
		 */
		g2.drawString(text, 0, 0);

		g2.setTransform(oldTransform);
		g2.setFont(oldFont);
		g2.setPaint(oldPaint);
	}
}