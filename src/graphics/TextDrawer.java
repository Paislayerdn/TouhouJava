package graphics;

import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.Font;
import java.awt.Paint;
import java.awt.GradientPaint;
import java.awt.RenderingHints;

import graphics.TextAlign;

public final class TextDrawer {
	private static final Font DEFAULT_FONT = new Font("Arial", Font.PLAIN, 18);

	// Utility class
	private TextDrawer() {}

	public static void draw(Graphics2D g2, String text, float x, float y) {
		draw(g2, text, x, y, DEFAULT_FONT, TextAlign.LEFT);
	}
	public static void draw(Graphics2D g2, String text, float x, float y, int size) {
		draw(g2, text, x, y, new Font("Arial", Font.PLAIN, size), TextAlign.LEFT);
	}
	public static void draw(Graphics2D g2, String text, float x, float y, TextAlign align) {
		draw(g2, text, x, y, DEFAULT_FONT, align);
	}
	public static void draw( Graphics2D g2, String text, float x, float y, int size, TextAlign align) {
		draw(
			g2,
			text,
			x,
			y,
			new Font("Arial", Font.PLAIN, size),
			align
		);
	}
	public static void draw(Graphics2D g2,
		String text,
		float x, float y,
		Font font, TextAlign align
	) {
		Font oldFont = g2.getFont();
		Paint oldPaint = g2.getPaint();
		var oldTransform = g2.getTransform();

		g2.setRenderingHint(
			RenderingHints.KEY_TEXT_ANTIALIASING,
			RenderingHints.VALUE_TEXT_ANTIALIAS_ON
		);

		g2.setFont(font);

		int width = g2.getFontMetrics().stringWidth(text);

		float offset = switch (align) {
			case LEFT -> 0;
			case CENTER -> -width / 2.0f;
			case RIGHT -> -width;
		};

		g2.translate(x, y);
		g2.scale(1, -1);

		g2.setPaint(new GradientPaint(
			0,
			-font.getSize(),
			Color.WHITE,
			0,
			0,
			new Color(80, 170, 255)
		));

		g2.drawString(text, offset, 0);

		g2.setTransform(oldTransform);
		g2.setFont(oldFont);
		g2.setPaint(oldPaint);
	}
}