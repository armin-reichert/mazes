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
public class Dir {

	public static final Dir N = new Dir(0, 0, -1);
	public static final Dir NE = new Dir(1, 1, -1);
	public static final Dir E = new Dir(2, 1, 0);
	public static final Dir SE = new Dir(3, 1, 1);
	public static final Dir S = new Dir(4, 0, 1);
	public static final Dir SW = new Dir(5, -1, 1);
	public static final Dir W = new Dir(6, -1, 0);
	public static final Dir NW = new Dir(7, -1, -1);

	/** The 4 directions North, East, South, West. */
	public static final Dir[] Dir4 = { N, E, S, W };

	/** The 4 directions North, East, South, West and in between. */
	public static final Dir[] Dir8 = { N, NE, E, SE, S, SW, W, NW };

	private static final Random rnd = new Random();

	private final int ordinal;
	private final int dx, dy;

	private Dir(int ordinal, int dx, int dy) {
		this.ordinal = ordinal;
		this.dx = dx;
		this.dy = dy;
	}

	public int dx() {
		return dx;
	}

	public int dy() {
		return dy;
	}

	public Dir right() {
		return Dir8[(ordinal + 2) % 8];
	}

	public Dir left() {
		return Dir8[(ordinal + 6) % 8];
	}

	public Dir inverse() {
		return Dir8[(ordinal + 4) % 8];
	}

	public static Dir[] permutedDir8Values() {
		List<Dir> dirs = new ArrayList<>(8);
		dirs.addAll(asList(Dir8));
		Collections.shuffle(dirs);
		return dirs.toArray(new Dir[8]);
	}

	public static Dir[] permutedDir4Values() {
		List<Dir> dirs = new ArrayList<>(4);
		dirs.addAll(asList(Dir4));
		Collections.shuffle(dirs);
		return dirs.toArray(new Dir[4]);
	}

	public static Dir randomDir4() {
		return Dir4[rnd.nextInt(4)];
	}

	public static Dir randomDir8() {
		return Dir8[rnd.nextInt(8)];
	}
}
