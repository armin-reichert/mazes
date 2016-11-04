package de.amr.easy.grid.iterators.curves;

import static de.amr.easy.grid.api.Direction4.E;
import static de.amr.easy.grid.api.Direction4.N;
import static de.amr.easy.grid.api.Direction4.S;
import static de.amr.easy.grid.api.Direction4.W;

import java.util.List;

import de.amr.easy.grid.api.Direction4;

/**
 * Computes a Hilbert curve as a list of directions.
 * <p>
 * The curve starts at the upper right corner and ends at the lower right corner.
 *
 * @author Armin Reichert
 */
public class HilbertCurve extends Curve<Direction4> {

	public HilbertCurve(int depth) {
		hilbert(depth, N, E, S, W);
	}

	public HilbertCurve(int i, Direction4 n, Direction4 e, Direction4 s, Direction4 w) {
		hilbert(i, n, e, s, w);
	}

	public HilbertCurve(int i, List<Direction4> dirs) {
		if (dirs.size() != 4) {
			throw new IllegalArgumentException();
		}
		hilbert(i, dirs.get(0), dirs.get(1), dirs.get(2), dirs.get(3));
	}

	private void hilbert(int i, Direction4 n, Direction4 e, Direction4 s, Direction4 w) {
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