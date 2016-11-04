package de.amr.easy.grid.iterators.curves;

import static de.amr.easy.grid.api.Direction4.E;
import static de.amr.easy.grid.api.Direction4.N;
import static de.amr.easy.grid.api.Direction4.S;
import static de.amr.easy.grid.api.Direction4.W;

import de.amr.easy.grid.api.Direction4;

/**
 * Computes a Peano-curve.
 * 
 * @author Armin Reichert
 */
public class PeanoCurve extends Curve<Direction4> {

	public PeanoCurve(int i) {
		peano(i, N, E, S, W);
	}

	private void peano(int i, Direction4 n, Direction4 e, Direction4 s, Direction4 w) {
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