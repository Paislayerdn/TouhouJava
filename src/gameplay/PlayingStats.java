package gameplay;

public final class PlayingStats {
	private static long score = 0;
	private static int graze = 0;
	private static int lives = 3;
	private static int bombs = 3;
	private static int power = 0;
	private static int deaths = 0;
	
	public static boolean debugMode = false;
	
	public static  void reset() {
		score = 0;
		graze = 0;
		lives = 3;
		bombs = 3;
		power = 0;
		deaths = 0;
	}
	
	public static void addGraze() { graze++; }

	public static void addDeath() { deaths++; }
	
	public static long getScore() {return score;}
	public static int getGraze() {return graze;}
	public static int getLives() {return lives;}
	public static int getBombs() {return bombs;}
	public static int getPower() {return power;}
	public static int getDeaths() {return deaths;}
}