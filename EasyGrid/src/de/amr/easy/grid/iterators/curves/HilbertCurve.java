package de.amr.easy.grid.iterators.curves;

import static de.amr.easy.grid.api.Direction.E;
import static de.amr.easy.grid.api.Direction.N;
import static de.amr.easy.grid.api.Direction.S;
import static de.amr.easy.grid.api.Direction.W;

import java.util.List;

import de.amr.easy.grid.api.Direction;

/**
 * Computes a Hilbert curve as a list of directions.
 *
 * @author Armin Reichert
 */
public class HilbertCurve extends Curve {

	public HilbertCurve(int depth) {
		hilbert(depth, N, E, S, W);
	}

	public HilbertCurve(int i, Direction n, Direction e, Direction s, Direction w) {
		hilbert(i, n, e, s, w);
	}

	public HilbertCurve(int i, List<Direction> dirs) {
		if (dirs.size() != 4) {
			throw new IllegalArgumentException();
		}
		hilbert(i, dirs.get(0), dirs.get(1), dirs.get(2), dirs.get(3));
	}

	private void hilbert(int i, Direction n, Direction e, Direction s, Direction w) {
		if (i > 0) {
			hilbert(i - 1, e, n, w, s);
			curve.add(w);
			hilbert(i - 1, n, e, s, w);
			curve.add(s);
			hilbert(i - 1, n, e, s, w);
			curve.add(e);
			hilbert(i - 1, w, s, e, n);
		}
	}
}