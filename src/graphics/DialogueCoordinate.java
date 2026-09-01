package graphics;

import java.awt.geom.Point2D;
import main.Settings;

public final class DialogueCoordinate {
	private DialogueCoordinate() {}

	public static double toScreenX(double x) {
		return Settings.WINDOW_WIDTH / 2.0 + x;
	}

	public static double toScreenY(double y) {
		return Settings.WINDOW_HEIGHT / 2.0 - y;
	}

	public static Point2D.Double toScreen(double x, double y) {
		return new Point2D.Double(
			toScreenX(x),
			toScreenY(y)
		);
	}
}