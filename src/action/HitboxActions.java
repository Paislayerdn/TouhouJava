package action;

import collision.Hitbox;
import static collision.Hitboxes.*;

import entity.Entity;

class AddCircleHitbox extends Action {
	private final String name;
	private final Object radius;
	@Override
	public boolean consumesFrame() { return false; }

	public AddCircleHitbox(String name, Object radius) {
		this.name = name;
		this.radius = radius;
	}

	@Override
	public void start() {
		if (owner.getHitbox(name) != null) {
			throw new IllegalArgumentException("[JScratch AddCircleHitbox] Hitbox already exists: " + name);
		}

		owner.addHitbox( circleHB(owner, name, resolveDouble(radius)) );

		finish();
	}
}

class AddRectangleHitbox extends Action {
	private final String name;
	private final Object width;
	private final Object height;
	@Override
	public boolean consumesFrame() {
		return false;
	}

	public AddRectangleHitbox(String name, Object width, Object height) {
		this.name = name;
		this.width = width;
		this.height = height;
	}


	@Override
	public void start() {
		if (owner.getHitbox(name) != null) {
			throw new IllegalArgumentException("[JScratch AddRectangleHitbox] Hitbox already exists: " + name);
		}

		owner.addHitbox(
			rectangleHB(owner, name, resolveDouble(width), resolveDouble(height))
		);

		finish();
	}
}

class SetHitboxEnabled extends Action {
	private final String name;
	private final boolean enabled;
	@Override
	public boolean consumesFrame() { return false; }

	public SetHitboxEnabled(String name, boolean enabled) {
		this.name = name;
		this.enabled = enabled;
	}


	@Override
	public void start() {
		Hitbox hitbox = owner.getHitbox(name);

		if (hitbox == null) {
			throw new IllegalArgumentException("[JScratch SetHitboxEnabled] Hitbox not found: " + name);
		}

		hitbox.setEnabled(enabled);
		finish();
	}
}

class SetHitboxTag extends Action {
	private final String hitboxName;
	private final String tag;
	private final boolean add;

	@Override
	public boolean consumesFrame() {
		return false;
	}

	public SetHitboxTag(String hitboxName, String tag, boolean add) {
		this.hitboxName = hitboxName;
		this.tag = tag;
		this.add = add;
	}

	@Override
	public void start() {
		Hitbox hitbox = owner.getHitbox(hitboxName);

		if (hitbox == null) {
			throw new IllegalArgumentException(
				"[JScratch SetHitboxTag] Hitbox not found: " + hitboxName
			);
		}

		if (add) {
			hitbox.addTag(tag);
		} else {
			hitbox.removeTag(tag);
		}

		finish();
	}
}