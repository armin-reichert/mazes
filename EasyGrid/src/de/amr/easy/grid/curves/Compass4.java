package de.amr.easy.grid.curves;

import de.amr.easy.grid.impl.Top4;

/**
 * A compas with 4 directions.
 * 
 * @author Armin Reichert
 */
public class Compass4 {

	private final int[] dirs;

	public Compass4(int... dirs) {
		if (dirs.length != 0 && dirs.length != 4) {
			throw new IllegalArgumentException("A compas must have 4 directions");
		}
		if (dirs.length == 0) {
			dirs = Top4.INSTANCE.dirs().toArray(); // N,E,S,W
		}
		this.dirs = dirs;
	}

	public Compass4 copy() {
		return new Compass4(dirs);
	}

	public int ahead() {
		return dirs[0];
	}

	public int right() {
		return dirs[1];
	}

	public int behind() {
		return dirs[2];
	}

	public int left() {
		return dirs[3];
	}

	public void turnLeft() {
		for (int i = 0; i < dirs.length; ++i) {
			dirs[i] = Top4.INSTANCE.left(dirs[i]);
		}
	}

	public void turnRight() {
		for (int i = 0; i < dirs.length; ++i) {
			dirs[i] = Top4.INSTANCE.right(dirs[i]);
		}
	}
}