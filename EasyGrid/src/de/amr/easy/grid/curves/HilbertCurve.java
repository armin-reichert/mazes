package de.amr.easy.grid.curves;

import static de.amr.easy.grid.api.dir.Dir4.E;
import static de.amr.easy.grid.api.dir.Dir4.N;
import static de.amr.easy.grid.api.dir.Dir4.S;
import static de.amr.easy.grid.api.dir.Dir4.W;

import de.amr.easy.grid.api.dir.Dir4;

/**
 * Computes a Hilbert curve as a list of directions.
 * <p>
 * The curve starts at the upper right corner and ends at the lower right corner.
 *
 * @author Armin Reichert
 */
public class HilbertCurve extends Curve<Dir4> {

	public HilbertCurve(int i) {
		hilbert(i, N, E, S, W);
	}

	public HilbertCurve(int i, Dir4 n, Dir4 e, Dir4 s, Dir4 w) {
		hilbert(i, n, e, s, w);
	}

	private void hilbert(int i, Dir4 n, Dir4 e, Dir4 s, Dir4 w) {
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