package de.amr.easy.grid.impl;

import java.util.stream.IntStream;

import de.amr.easy.grid.api.Topology;

/**
 * 4-direction topology for orthogonal grid.
 * 
 * @author Armin Reichert
 */
public class Top4 implements Topology {

	private static final int DX[] = { 0, 1, 0, -1 };
	private static final int DY[] = { -1, 0, 1, 0 };
	private static final String NAMES[] = { "North", "East", "South", "West" };

	/** North */
	public static final int N = 0;
	/** East */
	public static final int E = 1;
	/** South */
	public static final int S = 2;
	/** West */
	public static final int W = 3;

	Top4() {
	}

	@Override
	public IntStream dirs() {
		return IntStream.of(N, E, S, W);
	}

	@Override
	public int dirCount() {
		return 4;
	}

	@Override
	public String getName(int dir) {
		return NAMES[dir];
	}

	@Override
	public int inv(int dir) {
		return (dir + 2) % 4;
	}

	@Override
	public int left(int dir) {
		return (dir + 3) % 4;
	}

	@Override
	public int right(int dir) {
		return (dir + 1) % 4;
	}

	@Override
	public int dx(int dir) {
		return DX[dir];
	}

	@Override
	public int dy(int dir) {
		return DY[dir];
	};
}