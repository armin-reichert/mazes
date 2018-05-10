package de.amr.easy.grid.curves;

import static de.amr.easy.grid.impl.Topologies.TOP4;

/**
 * A compass with 4 directions.
 * 
 * @author Armin Reichert
 */
public class Compass4 {

	private final int[] dirs;

	public Compass4() {
		dirs = TOP4.dirs().toArray(); // N,E,S,W
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
			dirs[i] = TOP4.left(dirs[i]);
		}
	}

	public void turnRight() {
		for (int i = 0; i < dirs.length; ++i) {
			dirs[i] = TOP4.right(dirs[i]);
		}
	}
}