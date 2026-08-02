package game;

public final class GameStats {
	public static long score = 0;
	public static int graze = 0;
	public static int lives = 3;
	public static int bombs = 3;
	public static int power = 0;
	
	protected static int deaths = 0;
	
	public static boolean debugMode = false;
	
	public static void reset() {
		score = 0;
		graze = 0;
		lives = 3;
		bombs = 3;
		power = 0;
		deaths = 0;
	}
	
	public static void addGraze() { graze++; }

	public static void addDeath() { deaths++; }
}