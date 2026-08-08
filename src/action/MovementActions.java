package action;

import entity.Entity;

class GoToAction extends Action {
	private Entity target;
	@Override
	public boolean consumesFrame() { return false; }

	public GoToAction(Entity target) {
		this.target = target;
	}

	@Override
	public void start() {
		owner.setPosition(
			target.getX(),
			target.getY()
		);

		finish();
	}
}

class SetAction extends Action {
	public enum Axis {
		X,
		Y,
		BOTH
	}

	private final Object x;
	private final Object y;
	private final Axis axis;
	@Override
	public boolean consumesFrame() { return false; }

	// Set both X and Y
	public SetAction(Object x, Object y) {
		this.x = x;
		this.y = y;
		this.axis = Axis.BOTH;
	}

	// Set X or Y
	public SetAction(Object value, Axis axis) {
		this.axis = axis;

		if (axis == Axis.X) {
			this.x = value;
			this.y = null;
		} else {
			this.x = null;
			this.y = value;
		}
	}

	@Override
	public void update() {
		switch (axis) {
			case X:
				owner.setX(resolveDouble(x));
				break;

			case Y:
				owner.setY(resolveDouble(y));
				break;

			case BOTH:
				owner.setPosition(
					resolveDouble(x),
					resolveDouble(y)
				);
				break;
		}

		finish();
	}
}

class MoveAction extends Action {
	private final Object x;
	private final Object y;
	@Override
	public boolean consumesFrame() { return false; }

	public MoveAction(Object x, Object y) {
		this.x = x;
		this.y = y;
	}

	@Override
	public void update() {
		double dx = resolveDouble(x);
		double dy = resolveDouble(y);

		owner.move(dx, dy);

		finish();
	}
}

class ForwardAction extends Action {
	private Object distance;
	@Override
	public boolean consumesFrame() { return false; }

	public ForwardAction(Object distance) {
		this.distance = distance;
	}

	@Override
	public void update() {
		double ddDistance = resolveDouble(this.distance);

		double radians = Math.toRadians(owner.getTrueAngle());
		
		owner.setPosition(
			owner.getX() + Math.cos(radians) * ddDistance,
			owner.getY() + Math.sin(radians) * ddDistance
		);
		finish();
	}
}