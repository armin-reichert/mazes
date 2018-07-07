package de.amr.easy.grid.impl;

import java.util.stream.IntStream;

import de.amr.easy.grid.api.Topology;

/**
 * 4-direction topology for orthogonal grid.
 * 
 * @author Armin Reichert
 */
public class Top4 implements Topology {

	public static final int N = 0;
	public static final int E = 1;
	public static final int S = 2;
	public static final int W = 3;

	private static final int[][] VEC = { { 0, -1 }, { 1, 0 }, { 0, 1 }, { -1, 0 } };

	@Override
	public IntStream dirs() {
		return IntStream.of(N, E, S, W);
	}

	@Override
	public int dirCount() {
		return 4;
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
		return VEC[dir][0];
	}

	@Override
	public int dy(int dir) {
		return VEC[dir][1];
	};
}