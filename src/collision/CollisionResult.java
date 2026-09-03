package collision;

import entity.Entity;

public class CollisionResult {
	private Hitbox first;
	private Hitbox second;
	private CollisionType type;
	

	public CollisionResult(Hitbox first, Hitbox second, CollisionType type) {
		this.first = first;
		this.second = second;
		this.type = type;
	}
	
	private Hitbox getHitbox(Entity entity, boolean self) {
		if (first.getOwner() == entity)
			return self ? first : second;

		if (second.getOwner() == entity)
			return self ? second : first;

		return null;
	}
	public Hitbox getSelf(Entity entity) { return getHitbox(entity, true); }
	public Hitbox getOther(Entity entity) { return getHitbox(entity, false); }
	public CollisionType getType() { return type; }
}