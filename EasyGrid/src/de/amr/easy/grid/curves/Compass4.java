package de.amr.easy.grid.curves;

import static de.amr.easy.grid.impl.Top4.E;
import static de.amr.easy.grid.impl.Top4.N;
import static de.amr.easy.grid.impl.Top4.S;
import static de.amr.easy.grid.impl.Top4.W;

import de.amr.easy.grid.impl.Top4;

/**
 * A compass with 4 directions.
 * 
 * @author Armin Reichert
 */
public class Compass4 {

	private final int[] dirs;

	public Compass4() {
		dirs = new int[] { N, E, S, W };
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
			dirs[i] = Top4.get().left(dirs[i]);
		}
	}

	public void turnRight() {
		for (int i = 0; i < dirs.length; ++i) {
			dirs[i] = Top4.get().right(dirs[i]);
		}
	}
}