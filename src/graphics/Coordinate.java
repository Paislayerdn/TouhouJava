package graphics;

import java.awt.geom.Point2D;

import main.Settings;

public final class Coordinate {
	private Coordinate() {}

	/* ---------- World -> Screen ---------- */
	public static float toScreenX(float worldX) {
		return Settings.PLAYFIELD_CENTER_X + worldX;
	}
	public static float toScreenY(float worldY) {
		return Settings.PLAYFIELD_CENTER_Y + worldY;
	}

	public static Point2D.Float toScreen(
		float worldX,
		float worldY
	) {
		return new Point2D.Float(
			toScreenX(worldX),
			toScreenY(worldY)
		);
	}

	/* ---------- Screen -> World ---------- */
	public static float toWorldX(float screenX) {
		return screenX - Settings.PLAYFIELD_CENTER_X;
	}
	public static float toWorldY(float screenY) {
		return screenY - Settings.PLAYFIELD_CENTER_Y;
	}

	public static Point2D.Float toWorld(
		float screenX,
		float screenY
	) {
		return new Point2D.Float(
			toWorldX(screenX),
			toWorldY(screenY)
		);
	}

	/* ---------- Formatting ---------- */
	public static String format(float x, float y) {
		return format(x, y, 2);
	}

	public static String format(
		float x,
		float y,
		int decimals
	) {
		String pattern =
			"(x,y): (%." + decimals + "f, %."
			+ decimals + "f)";

		return String.format(pattern, x, y);
	}

	public static String format(Point2D.Float point) {
		return format(point.x, point.y);
	}

	public static String format(Point2D.Float point, int decimals) {
		return format(point.x, point.y, decimals);
	}
}