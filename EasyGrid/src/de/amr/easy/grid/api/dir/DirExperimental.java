package de.amr.easy.grid.api.dir;

import static java.util.Arrays.asList;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Direction inside an orthogonal grid.
 * 
 * @author Armin Reichert
 */
public class DirExperimental {

	public static final DirExperimental N = new DirExperimental(0, 0, 0, -1);
	public static final DirExperimental NE = new DirExperimental(1, -1, 1, -1);
	public static final DirExperimental E = new DirExperimental(2, 1, 1, 0);
	public static final DirExperimental SE = new DirExperimental(3, -1, 1, 1);
	public static final DirExperimental S = new DirExperimental(4, 2, 0, 1);
	public static final DirExperimental SW = new DirExperimental(5, -1, -1, 1);
	public static final DirExperimental W = new DirExperimental(6, 3, -1, 0);
	public static final DirExperimental NW = new DirExperimental(7, -1, -1, -1);

	/** The 4 directions North, East, South, West. */
	public static final DirExperimental[] Dir4 = { N, E, S, W };

	/** The 4 directions North, East, South, West and in between. */
	public static final DirExperimental[] Dir8 = { N, NE, E, SE, S, SW, W, NW };

	private static final Random rnd = new Random();

	private final int ordinal8;
	private final int ordinal4;
	private final int dx, dy;

	private DirExperimental(int ordinal8, int ordinal4, int dx, int dy) {
		this.ordinal8 = ordinal8;
		this.ordinal4 = ordinal4;
		this.dx = dx;
		this.dy = dy;
	}

	public int ordinal4() {
		return ordinal4;
	}

	public int ordinal8() {
		return ordinal8;
	}

	public int dx() {
		return dx;
	}

	public int dy() {
		return dy;
	}

	public DirExperimental right() {
		return Dir8[(ordinal8 + 2) % 8];
	}

	public DirExperimental left() {
		return Dir8[(ordinal8 + 6) % 8];
	}

	public DirExperimental inverse() {
		return Dir8[(ordinal8 + 4) % 8];
	}

	public static DirExperimental[] permutedDir8Values() {
		List<DirExperimental> dirs = new ArrayList<>(8);
		dirs.addAll(asList(Dir8));
		Collections.shuffle(dirs);
		return dirs.toArray(new DirExperimental[8]);
	}

	public static DirExperimental[] permutedDir4Values() {
		List<DirExperimental> dirs = new ArrayList<>(4);
		dirs.addAll(asList(Dir4));
		Collections.shuffle(dirs);
		return dirs.toArray(new DirExperimental[4]);
	}

	public static DirExperimental randomDir4() {
		return Dir4[rnd.nextInt(4)];
	}

	public static DirExperimental randomDir8() {
		return Dir8[rnd.nextInt(8)];
	}
}
