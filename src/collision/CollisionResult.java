package collision;

public class CollisionResult {
	private Hitbox first;
	private Hitbox second;

	public CollisionResult(Hitbox first, Hitbox second) {
		this.first = first;
		this.second = second;
	}

	public Hitbox getFirst() {
		return first;
	}

	public Hitbox getSecond() {
		return second;
	}
}