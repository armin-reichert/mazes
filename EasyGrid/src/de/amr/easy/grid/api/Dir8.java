package de.amr.easy.grid.api;

import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

/**
 * 8 Directions in a grid.
 */
public enum Dir8 {

	N(0, -1), NE(1, -1), E(1, 0), SE(1, 1), S(0, 1), SW(-1, 1), W(-1, 0), NW(-1, -1);

	private static final Dir8[] cachedValues = values();
	private static final Random rnd = new Random();

	private final int dx, dy;

	private Dir8(int dx, int dy) {
		this.dx = dx;
		this.dy = dy;
	}
	
	public int dx() {
		return dx;
	}
	
	public int dy() {
		return dy;
	}

	public Dir8 right() {
		return cachedValues[ordinal() < 7 ? ordinal() + 1 : 0];
	}

	public Dir8 left() {
		return cachedValues[ordinal() > 0 ? ordinal() - 1 : 7];
	}

	public Dir8 inverse() {
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

	public static Dir8[] valuesPermuted() {
		Dir8[] dirs = values(); // new copy needed
		Collections.shuffle(Arrays.asList(dirs));
		return dirs;
	}

	public static Dir8 randomValue() {
		return cachedValues[rnd.nextInt(8)];
	}
}