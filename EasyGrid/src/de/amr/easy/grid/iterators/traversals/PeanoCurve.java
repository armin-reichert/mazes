package de.amr.easy.grid.iterators.traversals;

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
public class PeanoCurve implements Iterable<Direction> {

	private final List<Direction> directions = new ArrayList<>();

	public PeanoCurve(int depth) {
		peano(depth, Direction.N, Direction.E, Direction.S, Direction.W);
	}

	private void peano(int n, Direction N, Direction E, Direction S, Direction W) {
		if (n > 0) {
			peano(n - 1, N, E, S, W);
			directions.add(N);
			peano(n - 1, N, W, S, E);
			directions.add(N);
			peano(n - 1, N, E, S, W);
			directions.add(E);
			peano(n - 1, S, E, N, W);
			directions.add(S);
			peano(n - 1, S, W, N, E);
			directions.add(S);
			peano(n - 1, S, E, N, W);
			directions.add(E);
			peano(n - 1, N, E, S, W);
			directions.add(N);
			peano(n - 1, N, W, S, E);
			directions.add(N);
			peano(n - 1, N, E, S, W);
		}
	}

	@Override
	public Iterator<Direction> iterator() {
		return directions.iterator();
	}

	public Stream<Direction> stream() {
		return directions.stream();
	}

}
