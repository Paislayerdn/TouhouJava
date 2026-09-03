package collision;

import static collision.CollisionTags.*;

public final class CollisionSemantics {
	private CollisionSemantics() {}

	public static CollisionType getType(Hitbox a, Hitbox b) {

		if (hasTags(a, PGRAZE) && hasTags(b, ENEMY_BULLET)
		 || hasTags(b, PGRAZE) && hasTags(a, ENEMY_BULLET)) {
			return CollisionType.GRAZE;
		}

		if (hasTags(a, PDEATH) && hasTags(b, ENEMY_BULLET)
		 || hasTags(b, PDEATH) && hasTags(a, ENEMY_BULLET)) {
			return CollisionType.DEATH;
		}

		if (hasTags(a, BOSS) && hasTags(b, PLAYER_BULLET)
		 || hasTags(b, BOSS) && hasTags(a, PLAYER_BULLET)) {
			return CollisionType.DAMAGE;
		}

		if (hasTags(a, BOMB) && hasTags(a, PLAYER_BULLET)
		 && hasTags(b, ENEMY_BULLET) && hasTags(b, CLEARABLE)
		 ||
		 hasTags(b, BOMB) && hasTags(b, PLAYER_BULLET)
		 && hasTags(a, ENEMY_BULLET) && hasTags(a, CLEARABLE)) {
			return CollisionType.DESTROY;
		}

		return null;
	}

	private static boolean hasTags(Hitbox hitbox, String... tags) {
		for (String tag : tags) {
			if (!hitbox.hasTag(tag))
				return false;
		}

		return true;
	}
}