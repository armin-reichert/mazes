package de.amr.easy.grid.impl;

import java.util.stream.IntStream;

import de.amr.easy.grid.api.Topology;

/**
 * 8-direction topology for orthogonal grid.
 * 
 * @author Armin Reichert
 */
public class Top8 implements Topology {

	public static final int N = 0;
	public static final int NE = 1;
	public static final int E = 2;
	public static final int SE = 3;
	public static final int S = 4;
	public static final int SW = 5;
	public static final int W = 6;
	public static final int NW = 7;

	private static final int[][] VEC = { { 0, -1 }, { 1, -1 }, { 1, 0 }, { 1, 1 }, { 0, 1 }, { -1, 1 }, { -1, 0 },
			{ -1, -1 } };

	public static Top8 get() {
		return new Top8();
	}

	@Override
	public IntStream dirs() {
		return IntStream.of(N, NE, E, SE, S, SW, W, NW);
	}

	@Override
	public int dirCount() {
		return 8;
	}

	@Override
	public int inv(int dir) {
		return (dir + 4) % 8;
	}

	@Override
	public int left(int dir) {
		return (dir + 7) % 8;
	}

	@Override
	public int right(int dir) {
		return (dir + 1) % 8;
	}

	@Override
	public int dx(int dir) {
		return VEC[dir][0];
	}

	@Override
	public int dy(int dir) {
		return VEC[dir][1];
	};
}