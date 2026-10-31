package state.gameplay;

import java.util.prefs.Preferences;

/** Stores the single game-wide high score on this computer. */
public final class HighScoreStore {
	private static final String KEY = "high-score";
	private static final Preferences PREFERENCES = Preferences.userNodeForPackage(HighScoreStore.class);

	private HighScoreStore() {}

	public static long record(long score) {
		long highScore = PREFERENCES.getLong(KEY, 0);
		if (score > highScore) {
			highScore = score;
			PREFERENCES.putLong(KEY, highScore);
		}
		return highScore;
	}
}
