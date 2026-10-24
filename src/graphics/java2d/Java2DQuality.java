package graphics.java2d;

public final class Java2DQuality {
	public enum RoundingMode {
		ROUND_UP,
		ROUND,
		ROUND_DOWN
	}

	public static float COLOR_STEP = 16.0f;
	public static RoundingMode COLOR_ROUNDING = RoundingMode.ROUND_UP;
	public static float DESATURATION_STEP = 16.0f;
	public static RoundingMode DESATURATION_ROUNDING = RoundingMode.ROUND_UP;
	public static float PIXELATE_STEP = 16.0f;
	public static RoundingMode PIXELATE_ROUNDING = RoundingMode.ROUND_UP;
	public static float BRIGHTNESS_STEP = 16.0f;
	public static RoundingMode BRIGHTNESS_ROUNDING = RoundingMode.ROUND_UP;

	private Java2DQuality() {}
}
