package de.amr.easy.grid.api.dir;

import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

/**
 * Directions in an orthogonal grid.
 */
public enum Dir4 {

	N(0, -1), E(1, 0), S(0, 1), W(-1, 0);

	private static final Dir4[] cachedValues = values();
	private static final Random rnd = new Random();

	private final int dx, dy;

	private Dir4(int dx, int dy) {
		this.dx = dx;
		this.dy = dy;
	}

	public int dx() {
		return dx;
	}

	public int dy() {
		return dy;
	}

	public Dir4 right() {
		return cachedValues[ordinal() < 3 ? ordinal() + 1 : 0];
	}

	public Dir4 left() {
		return cachedValues[ordinal() > 0 ? ordinal() - 1 : 3];
	}

	public Dir4 inverse() {
		switch (this) {
		case N:
			return S;
		case S:
			return N;
		case W:
			return E;
		case E:
			return W;
		default:
			throw new IllegalStateException();
		}
	}

	public static Dir4[] valuesPermuted() {
		Dir4[] dirs = values(); // new copy needed
		Collections.shuffle(Arrays.asList(dirs));
		return dirs;
	}

	public static Dir4 randomValue() {
		return cachedValues[rnd.nextInt(4)];
	}
}