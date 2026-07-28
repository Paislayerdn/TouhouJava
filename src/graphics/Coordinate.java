package graphics;

import java.awt.geom.Point2D;

import main.Settings;

public final class Coordinate {
	private Coordinate() {}

	/* ---------- World -> Screen ---------- */

	public static double toScreenX(double worldX) {
		return Settings.PLAYFIELD_X
				+ Settings.PLAYFIELD_WIDTH / 2.0
				+ worldX;
	}

	public static double toScreenY(double worldY) {
		return Settings.PLAYFIELD_Y
				+ Settings.PLAYFIELD_HEIGHT / 2.0
				- worldY;
	}

	public static Point2D.Double toScreen(double worldX, double worldY) {
		return new Point2D.Double( toScreenX(worldX),toScreenY(worldY) );
	}

	/* ---------- Screen -> World ---------- */

	public static double toWorldX(double screenX) {
		return screenX
				- Settings.PLAYFIELD_X
				- Settings.PLAYFIELD_WIDTH / 2.0;
	}

	public static double toWorldY(double screenY) {
		return -(screenY
				- Settings.PLAYFIELD_Y
				- Settings.PLAYFIELD_HEIGHT / 2.0);
	}

	public static Point2D.Double toWorld(double screenX, double screenY) {
		return new Point2D.Double(toWorldX( screenX), toWorldY(screenY) );
	}

	/* ---------- Formatting ---------- */

	public static String format(double x, double y) {
		return format(x, y, 2);
	}

	public static String format(double x, double y, int decimals) {
		String pattern = "(x,y): (%." + decimals + "f, %." + decimals + "f)";

		return String.format( pattern, x, y );
	}

	public static String format(Point2D.Double point) {
		return format(point.x, point.y);
	}

	public static String format(Point2D.Double point, int decimals) {
		return format(point.x, point.y, decimals);
	}
}