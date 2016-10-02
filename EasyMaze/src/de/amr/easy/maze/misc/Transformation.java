package de.amr.easy.maze.misc;

import de.amr.easy.grid.api.Direction;

/**
 * Transformation defined by permutation of directions.
 * 
 * @author Armin Reichert
 */
public class Transformation {

	public static Transformation IDENTITY = new Transformation(Direction.values());

	private final Direction[] permuted = new Direction[4];

	public Transformation(Direction... values) {
		if (values.length != 4)
			throw new IllegalArgumentException();
		for (int i = 0; i < 4; i++) {
			permuted[i] = values[i];
		}
	}

	/**
	 * 
	 * @param d
	 *          some direction
	 * @return result of applying this permutation to the given direction
	 */
	public Direction apply(Direction d) {
		return permuted[d.ordinal()];
	}

	/**
	 * 
	 * @param p
	 *          some permutation
	 * @return permutation which results from applying this permutation after the given permutation
	 */
	public Transformation after(Transformation p) {
		if (this == IDENTITY)
			return p == IDENTITY ? IDENTITY : p;
		if (p == IDENTITY)
			return this;
		Direction[] product = new Direction[4];
		for (Direction d : Direction.values()) {
			product[d.ordinal()] = p.apply(apply(d));
		}
		return new Transformation(product);
	}
}
