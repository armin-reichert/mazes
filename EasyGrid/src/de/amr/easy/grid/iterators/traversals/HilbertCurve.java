package de.amr.easy.grid.iterators.traversals;

import static java.lang.System.out;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import de.amr.easy.grid.api.Direction;

/**
 * Computes a Hilbert curve.
 * 
 * @author Armin Reichert
 */
public class HilbertCurve implements Iterable<Direction> {

	private final List<Direction> directions = new ArrayList<>();

	public HilbertCurve(int depth) {
		hilbert(depth, Direction.N, Direction.E, Direction.S, Direction.W);
	}

	public HilbertCurve(int depth, Direction n, Direction e, Direction s, Direction w) {
		hilbert(depth, n, e, s, w);
	}

	private void hilbert(int depth, Direction n, Direction e, Direction s, Direction w) {
		if (depth > 0) {
			hilbert(depth - 1, e, n, w, s);
			directions.add(w);
			hilbert(depth - 1, n, e, s, w);
			directions.add(s);
			hilbert(depth - 1, n, e, s, w);
			directions.add(e);
			hilbert(depth - 1, w, s, e, n);
		}
	}

	@Override
	public Iterator<Direction> iterator() {
		return directions.iterator();
	}

	@Override
	public String toString() {
		StringBuilder s = new StringBuilder();
		for (Direction dir : directions) {
			s.append(dir.name()).append(" ");
		}
		return s.toString();
	}

	public static void main(String[] args) {
		// IntStream.rangeClosed(1, 3).forEach(depth -> out.println(new HilbertCurve(depth)));
		// IntStream.rangeClosed(1, 3).forEach(depth -> out.println(new HilbertCurve(depth, N, E, S,
		// W)));
		// IntStream.rangeClosed(1, 3).forEach(depth -> out.println(new HilbertCurve(depth, E, S, W,
		// N)));
		new HilbertCurve(2);
		out.println("Fertig");
	}
}
