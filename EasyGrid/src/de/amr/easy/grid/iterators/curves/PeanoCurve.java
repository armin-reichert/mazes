package de.amr.easy.grid.iterators.curves;

import static de.amr.easy.grid.api.Direction.E;
import static de.amr.easy.grid.api.Direction.N;
import static de.amr.easy.grid.api.Direction.S;
import static de.amr.easy.grid.api.Direction.W;

import de.amr.easy.grid.api.Direction;

/**
 * Computes a Peano-curve.
 * 
 * @author Armin Reichert
 */
public class PeanoCurve extends Curve {

	public PeanoCurve(int i) {
		peano(i, N, E, S, W);
	}

	private void peano(int i, Direction n, Direction e, Direction s, Direction w) {
		if (i > 0) {
			peano(i - 1, n, e, s, w);
			curve.add(n);
			peano(i - 1, n, w, s, e);
			curve.add(n);
			peano(i - 1, n, e, s, w);
			curve.add(e);
			peano(i - 1, s, e, n, w);
			curve.add(s);
			peano(i - 1, s, w, n, e);
			curve.add(s);
			peano(i - 1, s, e, n, w);
			curve.add(e);
			peano(i - 1, n, e, s, w);
			curve.add(n);
			peano(i - 1, n, w, s, e);
			curve.add(n);
			peano(i - 1, n, e, s, w);
		}
	}
}