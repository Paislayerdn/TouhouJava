package state.gameplay;

import entity.Item;
import entity.Player;
import graphics.Renderer;
import java.util.ArrayList;
import java.util.List;
import main.Settings;

/** Owns the small set of collectible items dropped by a spell encounter. */
public final class ItemManager {
	private static final float COLLECT_RADIUS_SQUARED = 22 * 22;
	private static final float COLLECTION_LINE_Y = Settings.PLAYFIELD_BOTTOM
		+ Settings.PLAYFIELD_HEIGHT * 0.8f;
	private final ArrayList<Item> items = new ArrayList<>();
	private final PlayingStats stats;

	public ItemManager(PlayingStats stats) {
		this.stats = stats;
	}

	public void dropSpellItems(float x, float y) {
		drop(Item.Type.POWER, 12, x, y);
		drop(Item.Type.POINT, 18, x, y);
		drop(Item.Type.BOMB, 1, x, y);
	}

	private void drop(Item.Type type, int count, float x, float y) {
		for (int index = 0; index < count; index++) {
			float angle = (float) (Math.PI * 2.0 * index / count);
			float speed = 1.1f + (index % 3) * 0.35f;
			items.add(new Item(type, x, y,
				(float) Math.cos(angle) * speed,
				(float) Math.sin(angle) * speed + 1.2f));
		}
	}

	public void update(Player player) {
		for (Item item : new ArrayList<>(items)) {
			if (player.getY() >= COLLECTION_LINE_Y || item.isHoming()) {
				item.homeTo(player.getX(), player.getY());
			}
			item.update();
			float dx = item.getX() - player.getX();
			float dy = item.getY() - player.getY();
			if (dx * dx + dy * dy <= COLLECT_RADIUS_SQUARED) {
				collect(item, player.getY());
				item.destroy();
			}
		}
		items.removeIf(item -> !item.isAlive()
			|| item.getY() < Settings.PLAYFIELD_BOTTOM - 40);
	}

	private void collect(Item item, float playerY) {
		switch (item.getType()) {
			case POINT -> stats.addScore(pointItemValue(playerY));
			case POWER -> stats.addPower(1);
			case BOMB -> stats.addBomb();
		}
	}

	private long pointItemValue(float playerY) {
		// Point item values are tuned in one place; height scales linearly to the collection line.
		long minimumValue = 10_000;
		long maximumValue = 1_000_000;
		float lowerLine = Settings.PLAYFIELD_BOTTOM;
		float upperLine = COLLECTION_LINE_Y;
		float position = Math.max(0, Math.min(1,
			(playerY - lowerLine) / (upperLine - lowerLine)));
		return minimumValue + Math.round((maximumValue - minimumValue) * position);
	}

	public void draw(Renderer renderer) {
		for (Item item : new ArrayList<>(items)) item.draw(renderer);
	}

	public int size() { return items.size(); }
}
