package de.amr.easy.grid.api;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Directions in an orthogonal grid.
 */
public enum Direction {

	N(0, -1), E(1, 0), S(0, 1), W(-1, 0);

	public final int dx, dy;

	private Direction(int dx, int dy) {
		this.dx = dx;
		this.dy = dy;
	}

	public Direction right() {
		return values()[ordinal() < 3 ? ordinal() + 1 : 0];
	}

	public Direction left() {
		return values()[ordinal() > 0 ? ordinal() - 1 : 3];
	}

	public Direction inverse() {
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

	public static Direction[] randomOrder() {
		List<Direction> dirs = Arrays.asList(Direction.values());
		Collections.shuffle(dirs);
		return dirs.toArray(new Direction[dirs.size()]);
	}

	public static Direction randomValue() {
		return values()[new Random().nextInt(4)];
	}
}