package state.gameplay;

public final class PlayingStats {
	private static final int INITIAL_LIVES = 3;
	private static final int INITIAL_BOMBS = 3;
	private static final long GRAZE_SCORE_REWARD = 10;
	private long score;
	private int graze;
	private int lives;
	private int bombs;
	private int power;
	private int deaths;
	
	public static boolean debugMode = false;
	
	public void reset() {
		score = 0;
		graze = 0;
		lives = INITIAL_LIVES;
		bombs = INITIAL_BOMBS;
		power = 0;
		deaths = 0;
	}
	
	public PlayingStats() { reset(); }

	public void addScore(long amount) {
		if (amount > 0) score += amount;
	}

	public void addGraze() {
		graze++;
		addScore(GRAZE_SCORE_REWARD);
	}
	public void addPower(int amount) { power = Math.max(0, power + amount); }
	public void addBomb() { bombs++; }
	public boolean useBomb() {
		if (bombs <= 0) return false;
		bombs--;
		return true;
	}
	public boolean loseLife() {
		deaths++;
		if (lives > 0) lives--;
		return lives > 0;
	}
	public void restoreMinimumBombsAfterDeath() {
		if (bombs < INITIAL_BOMBS) bombs = INITIAL_BOMBS;
	}

	public long getScore() {return score;}
	public int getGraze() {return graze;}
	public int getLives() {return lives;}
	public int getBombs() {return bombs;}
	public int getPower() {return power;}
	public int getDeaths() {return deaths;}
}
