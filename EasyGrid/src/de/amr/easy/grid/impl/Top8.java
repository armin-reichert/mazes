package de.amr.easy.grid.impl;

import java.util.stream.IntStream;

import de.amr.easy.grid.api.Topology;

/**
 * 8-direction topology for orthogonal grid.
 * 
 * @author Armin Reichert
 */
public class Top8 implements Topology {

	private static final int DX[] = { 0, 1, 1, 1, 0, -1, -1, -1 };
	private static final int DY[] = { -1, -1, 0, 1, 1, 1, 0, -1 };
	private static final String NAMES[] = { "N", "NE", "E", "SE", "S", "SW", "W", "NW" };

	public static final int N = 0;
	public static final int NE = 1;
	public static final int E = 2;
	public static final int SE = 3;
	public static final int S = 4;
	public static final int SW = 5;
	public static final int W = 6;
	public static final int NW = 7;

	@Override
	public IntStream dirs() {
		return IntStream.of(N, NE, E, SE, S, SW, W, NW);
	}

	@Override
	public int dirCount() {
		return 8;
	}

	@Override
	public String getName(int dir) {
		return NAMES[dir];
	}

	@Override
	public int ord(int dir) {
		return dir;
	}

	@Override
	public int inv(int dir) {
		return (dir + 2) % 8;
	}

	@Override
	public int left(int dir) {
		return (dir + 3) % 8;
	}

	@Override
	public int right(int dir) {
		return (dir + 1) % 8;
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