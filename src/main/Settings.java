package main;

public final class Settings {

    // Prevent instantiation
    private Settings() {}

    // Base game resolution
    public static final int GAME_WIDTH = 960;
    public static final int GAME_HEIGHT = 720;

    // Window scaling
    public static final int SCALE = 1;

    // Final window size
    public static final int WINDOW_WIDTH = GAME_WIDTH * SCALE;
    public static final int WINDOW_HEIGHT = GAME_HEIGHT * SCALE;

    // Target FPS
    public static final int FPS = 60;
}