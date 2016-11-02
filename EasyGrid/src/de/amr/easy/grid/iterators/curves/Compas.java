package de.amr.easy.grid.iterators.curves;

import de.amr.easy.grid.api.Direction;

/**
 * A compas that can be turned clock- and counterclockwise.
 * 
 * @author Armin Reichert
 */
public class Compas {

	private final Direction[] dirs;

	public Compas(Direction... dirs) {
		if (dirs.length != 0 && dirs.length != 4) {
			throw new IllegalArgumentException("A compas must have 4 directions");
		}
		if (dirs.length == 0) {
			dirs = Direction.values(); // N,E,S,W
		}
		this.dirs = dirs;
	}
	
	public Compas copy() {
		return new Compas(dirs);
	}

	public Direction ahead() {
		return dirs[0];
	}

	public Direction right() {
		return dirs[1];
	}

	public Direction behind() {
		return dirs[2];
	}

	public Direction left() {
		return dirs[3];
	}

	public Compas turnLeft() {
		for (int i = 0; i < dirs.length; ++i) {
			dirs[i] = dirs[i].left();
		}
		return this;
	}

	public Compas turnRight() {
		for (int i = 0; i < dirs.length; ++i) {
			dirs[i] = dirs[i].right();
		}
		return this;
	}
}