package de.amr.easy.grid.iterators.curves;

import de.amr.easy.grid.api.Dir4;

/**
 * A compas with 4 directions.
 * 
 * @author Armin Reichert
 */
public class Compas4 {

	private final Dir4[] dirs;

	public Compas4(Dir4... dirs) {
		if (dirs.length != 0 && dirs.length != 4) {
			throw new IllegalArgumentException("A compas must have 4 directions");
		}
		if (dirs.length == 0) {
			dirs = Dir4.values(); // N,E,S,W
		}
		this.dirs = dirs;
	}

	public Compas4 copy() {
		return new Compas4(dirs);
	}

	public Dir4 ahead() {
		return dirs[0];
	}

	public Dir4 right() {
		return dirs[1];
	}

	public Dir4 behind() {
		return dirs[2];
	}

	public Dir4 left() {
		return dirs[3];
	}

	public void turnLeft() {
		for (int i = 0; i < dirs.length; ++i) {
			dirs[i] = dirs[i].left();
		}
	}

	public void turnRight() {
		for (int i = 0; i < dirs.length; ++i) {
			dirs[i] = dirs[i].right();
		}
	}
}