package action;

import entity.Bullet;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import state.gameplay.BulletManager;

public final class SpawnRegistry {
	@FunctionalInterface
	public interface Factory {
		ActionTarget create(String requestedName, long uniqueId);
	}

	private static final Map<String, Factory> factories = new HashMap<>();
	private static final AtomicLong nextId = new AtomicLong();

	static {
		register("Bullet", (name, id) -> {
			Bullet bullet = new Bullet();
			bullet.setName(name + "#" + id);
			return bullet;
		});
	}

	private SpawnRegistry() {}

	public static synchronized void register(String className, Factory factory) {
		String key = className.toUpperCase(Locale.ROOT);
		if (factories.putIfAbsent(key, factory) != null) {
			throw new IllegalArgumentException("Spawn type already registered: " + className);
		}
	}

	public static ActionTarget create(String className, String requestedName) {
		Factory factory = factories.get(className.toUpperCase(Locale.ROOT));
		if (factory == null) throw new IllegalArgumentException(
			"No spawn factory registered for " + className);
		return factory.create(requestedName, nextId.incrementAndGet());
	}

	public static void activate(ActionTarget target) {
		if (target instanceof Bullet bullet) BulletManager.spawn(bullet);
		target.onBestowed();
	}
}
