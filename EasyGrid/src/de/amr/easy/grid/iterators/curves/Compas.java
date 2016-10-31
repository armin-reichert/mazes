package de.amr.easy.grid.iterators.curves;

import de.amr.easy.grid.api.Direction;

/**
 * A compas that can be turned by clockwise and counterclockwise.
 * 
 * @author Armin Reichert
 */
public class Compas {

	private final Direction[] dirs;

	public Compas(Direction... dirs) {
		if (dirs.length == 0) {
			dirs = Direction.values(); // N,E,S,W
		}
		this.dirs = dirs;
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
		return new Compas(ahead().left(), right().left(), behind().left(), left().left());
	}

	public Compas turnRight() {
		return new Compas(ahead().right(), right().right(), behind().right(), left().right());
	}
}