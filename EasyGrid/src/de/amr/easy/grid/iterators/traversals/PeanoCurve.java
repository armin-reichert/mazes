package de.amr.easy.grid.iterators.traversals;

import static de.amr.easy.grid.api.Direction.E;
import static de.amr.easy.grid.api.Direction.N;
import static de.amr.easy.grid.api.Direction.S;
import static de.amr.easy.grid.api.Direction.W;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

import de.amr.easy.grid.api.Direction;

/**
 * Computes a Peano-curve.
 * 
 * @author Armin Reichert
 */
public class PeanoCurve implements Sequence<Direction> {

	private final List<Direction> curve = new ArrayList<>();

	public PeanoCurve(int depth) {
		peano(depth, N, E, S, W);
	}

	private void peano(int n, Direction N, Direction E, Direction S, Direction W) {
		if (n > 0) {
			peano(n - 1, N, E, S, W);
			curve.add(N);
			peano(n - 1, N, W, S, E);
			curve.add(N);
			peano(n - 1, N, E, S, W);
			curve.add(E);
			peano(n - 1, S, E, N, W);
			curve.add(S);
			peano(n - 1, S, W, N, E);
			curve.add(S);
			peano(n - 1, S, E, N, W);
			curve.add(E);
			peano(n - 1, N, E, S, W);
			curve.add(N);
			peano(n - 1, N, W, S, E);
			curve.add(N);
			peano(n - 1, N, E, S, W);
		}
	}

	@Override
	public Iterator<Direction> iterator() {
		return curve.iterator();
	}

	@Override
	public Stream<Direction> stream() {
		return curve.stream();
	}
}