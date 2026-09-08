package main;

public final class Settings {
	private Settings() {}

	// Base game resolution
	public static final int BASE_WIDTH = 960;
	public static final int BASE_HEIGHT = 720;

	public static final int HALF_WIDTH = BASE_WIDTH / 2;
	public static final int HALF_HEIGHT = BASE_HEIGHT / 2;

	// Window scaling
	public static double SCALE = 1;
	public static int getWindowWidth() {return (int)(BASE_WIDTH * SCALE);}
	public static int getWindowHeight() {return (int)(BASE_HEIGHT * SCALE);}
	
	// Game rendering
	public static final int TILE_SIZE = 24;

	// Playfield
	public static final int PLAYFIELD_WIDTH = 24 * TILE_SIZE;   // 576
	public static final int PLAYFIELD_HEIGHT = 28 * TILE_SIZE;  // 672

	public static final int PLAYFIELD_HALF_WIDTH = PLAYFIELD_WIDTH / 2;
	public static final int PLAYFIELD_HALF_HEIGHT = PLAYFIELD_HEIGHT / 2;

	/*
	 * Playfield position in GAME coordinates.
	 *
	 * (0, 0) is the center of the entire screen.
	 * +X goes right.
	 * +Y goes up.
	 */
	public static final double PLAYFIELD_CENTER_X = -144;
	public static final double PLAYFIELD_CENTER_Y = 0;
	public static final double PLAYFIELD_LEFT = PLAYFIELD_CENTER_X - PLAYFIELD_HALF_WIDTH;
	public static final double PLAYFIELD_RIGHT = PLAYFIELD_CENTER_X + PLAYFIELD_HALF_WIDTH;
	public static final double PLAYFIELD_BOTTOM = PLAYFIELD_CENTER_Y - PLAYFIELD_HALF_HEIGHT;
	public static final double PLAYFIELD_TOP = PLAYFIELD_CENTER_Y + PLAYFIELD_HALF_HEIGHT;
	
	// Target FPS
	public static final int FPS = 60;
}