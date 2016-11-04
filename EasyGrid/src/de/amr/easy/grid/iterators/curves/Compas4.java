package de.amr.easy.grid.iterators.curves;

import de.amr.easy.grid.api.Direction4;

/**
 * A compas with 4 directions.
 * 
 * @author Armin Reichert
 */
public class Compas4 implements Compas<Direction4> {

	private final Direction4[] dirs;

	public Compas4(Direction4... dirs) {
		if (dirs.length != 0 && dirs.length != 4) {
			throw new IllegalArgumentException("A compas must have 4 directions");
		}
		if (dirs.length == 0) {
			dirs = Direction4.values(); // N,E,S,W
		}
		this.dirs = dirs;
	}

	public Compas4 copy() {
		return new Compas4(dirs);
	}

	@Override
	public Direction4 ahead() {
		return dirs[0];
	}

	@Override
	public Direction4 right() {
		return dirs[1];
	}

	@Override
	public Direction4 behind() {
		return dirs[2];
	}

	@Override
	public Direction4 left() {
		return dirs[3];
	}

	@Override
	public void turnLeft() {
		for (int i = 0; i < dirs.length; ++i) {
			dirs[i] = dirs[i].left();
		}
	}

	@Override
	public void turnRight() {
		for (int i = 0; i < dirs.length; ++i) {
			dirs[i] = dirs[i].right();
		}
	}
}