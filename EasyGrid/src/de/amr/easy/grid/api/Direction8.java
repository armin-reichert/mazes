package de.amr.easy.grid.api;

import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

/**
 * 8 Directions in a grid.
 */
public enum Direction8 {

	N(0, -1), NE(1, -1), E(1, 0), SE(1, 1), S(0, 1), SW(-1, 1), W(-1, 0), NW(-1, -1);

	private static final Direction8[] cachedValues = values();
	private static final Random rnd = new Random();

	public final int dx, dy;

	private Direction8(int dx, int dy) {
		this.dx = dx;
		this.dy = dy;
	}

	public Direction8 right() {
		return cachedValues[ordinal() < 7 ? ordinal() + 1 : 0];
	}

	public Direction8 left() {
		return cachedValues[ordinal() > 0 ? ordinal() - 1 : 7];
	}

	public Direction8 inverse() {
		switch (this) {
		case N:
			return S;
		case NE:
			return SW;
		case E:
			return W;
		case SE:
			return NW;
		case S:
			return N;
		case SW:
			return NE;
		case W:
			return E;
		case NW:
			return SE;
		default:
			throw new IllegalStateException();
		}
	}

	public static Direction8[] valuesPermuted() {
		Direction8[] dirs = values(); // new copy needed
		Collections.shuffle(Arrays.asList(dirs));
		return dirs;
	}

	public static Direction8 randomValue() {
		return cachedValues[rnd.nextInt(8)];
	}
}