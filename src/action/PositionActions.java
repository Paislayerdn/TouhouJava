package action;

import entity.Entity;

enum Axis {X, Y, BOTH}
class PositionAction extends Action {
    public enum Operation {SET, MOVE, FORWARD, GOTO}

    private final Operation operation;
    private final Object x;
    private final Object y;
    private final Entity target;

    public PositionAction(Object x, Object y) {
        this.operation = Operation.SET;
        this.x = x;
        this.y = y;
        this.target = null;
    }

    public PositionAction(Object value, Axis axis) {
        this.operation = Operation.SET;
        this.target = null;

        if (axis == Axis.X) {
            this.x = value;
            this.y = null;
        } else {
            this.x = null;
            this.y = value;
        }
    }

    public PositionAction(Operation operation, Object x, Object y) {
        this.operation = operation;
        this.x = x;
        this.y = y;
        this.target = null;
    }

    public PositionAction(Entity target) {
        this.operation = Operation.GOTO;
        this.target = target;
        this.x = null;
        this.y = null;
    }

    public PositionAction(Object distance) {
        this.operation = Operation.FORWARD;
        this.x = distance;
        this.y = null;
        this.target = null;
    }

    @Override
    public boolean consumesFrame() {
        return false;
    }

    @Override
    public void update() {
        switch (operation) {
            case SET:
                if (x != null && y != null) {
                    owner.setXY(
                        resolveDouble(x),
                        resolveDouble(y)
                    );
                } else if (x != null) {
                    owner.setX(resolveDouble(x));
                } else {
                    owner.setY(resolveDouble(y));
                }
                break;

            case MOVE:
                owner.move(
                    resolveDouble(x),
                    resolveDouble(y)
                );
                break;

            case FORWARD:
                double distance = resolveDouble(x);
                double radians = Math.toRadians(owner.getTrueAngle());

                owner.move(
                    Math.cos(radians) * distance,
                    Math.sin(radians) * distance
                );
                break;

            case GOTO:
                owner.setXY(
                    target.getX(),
                    target.getY()
                );
                break;
        }

        finish();
    }
}
