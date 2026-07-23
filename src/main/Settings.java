package main;

public final class Settings {

    // Prevent instantiation
    private Settings() {}

    // Base game resolution
	public static final int BASE_WIDTH = 960;
    public static final int BASE_HEIGHT = 720;
    
	// Window scaling
    public static final int SCALE = 1;
	
	// Game rendering things
	public static final int TILE_SIZE = 24;

	//starts of playfield's top left corner
	public static final int PLAYFIELD_X = 2 * TILE_SIZE;
	public static final int PLAYFIELD_Y = 1 * TILE_SIZE;

	public static final int PLAYFIELD_WIDTH = 24 * TILE_SIZE; //576
	public static final int PLAYFIELD_HEIGHT = 28 * TILE_SIZE; //672
	
	public static final int PLAYFIELD_HALF_WIDTH =PLAYFIELD_WIDTH / 2;
	public static final int PLAYFIELD_HALF_HEIGHT = PLAYFIELD_HEIGHT / 2;

    // Final window size
    public static final int WINDOW_WIDTH = BASE_WIDTH * SCALE;
    public static final int WINDOW_HEIGHT = BASE_HEIGHT * SCALE;

    // Target FPS
    public static final int FPS = 60;
}