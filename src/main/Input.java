package main;

import java.util.EnumSet;

public final class Input {
	public enum Key {
		W, S, A, D,
		UP, DOWN, LEFT, RIGHT,
		SPACE, Z, X,
		P, Q, PAGEUP,
		MOUSE_LEFT
	}

	public static final class Keys {
		public volatile boolean W;
		public volatile boolean S;
		public volatile boolean A;
		public volatile boolean D;
		public volatile boolean UP;
		public volatile boolean DOWN;
		public volatile boolean LEFT;
		public volatile boolean RIGHT;
		public volatile boolean SPACE;
		public volatile boolean Z;
		public volatile boolean X;
		public volatile boolean P;
		public volatile boolean Q;
		public volatile boolean PAGEUP;
		public volatile boolean MOUSE_LEFT;

		private void set(Key key, boolean value) {
			switch (key) {
				case W -> W = value;
				case S -> S = value;
				case A -> A = value;
				case D -> D = value;
				case UP -> UP = value;
				case DOWN -> DOWN = value;
				case LEFT -> LEFT = value;
				case RIGHT -> RIGHT = value;
				case SPACE -> SPACE = value;
				case Z -> Z = value;
				case X -> X = value;
				case P -> P = value;
				case Q -> Q = value;
				case PAGEUP -> PAGEUP = value;
				case MOUSE_LEFT -> MOUSE_LEFT = value;
			}
		}

		private void clear() {
			for (Key key : Key.values()) {
				set(key, false);
			}
		}
	}

	public static final Keys hold = new Keys();
	public static final Keys tap = new Keys();

	public static volatile float mouseX;
	public static volatile float mouseY;

	private static final EnumSet<Key> keysDown =
		EnumSet.noneOf(Key.class);

	private static final EnumSet<Key> pendingTaps =
		EnumSet.noneOf(Key.class);

	private Input() {}

	public static synchronized void keyPressed(Key key) {
		if (keysDown.add(key)) {
			pendingTaps.add(key);
		}

		hold.set(key, true);
	}

	public static synchronized void keyReleased(Key key) {
		keysDown.remove(key);
		hold.set(key, false);
	}

	public static synchronized void update() {
		tap.clear();

		for (Key key : pendingTaps) {
			tap.set(key, true);
		}

		pendingTaps.clear();
	}

	public static synchronized void clear() {
		keysDown.clear();
		pendingTaps.clear();

		hold.clear();
		tap.clear();
	}
}
