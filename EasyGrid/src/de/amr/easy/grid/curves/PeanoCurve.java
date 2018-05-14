package de.amr.easy.grid.curves;

import static de.amr.easy.grid.impl.Top4.E;
import static de.amr.easy.grid.impl.Top4.N;
import static de.amr.easy.grid.impl.Top4.S;
import static de.amr.easy.grid.impl.Top4.W;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import de.amr.easy.grid.api.Curve;

/**
 * Computes a Peano-curve.
 * 
 * @author Armin Reichert
 */
public class PeanoCurve implements Curve {

	private final List<Integer> dirs = new ArrayList<>();

	@Override
	public Iterator<Integer> iterator() {
		return dirs.iterator();
	}

	public PeanoCurve(int i) {
		peano(i, N, E, S, W);
	}

	private void peano(int i, int n, int e, int s, int w) {
		if (i > 0) {
			peano(i - 1, n, e, s, w);
			dirs.add(n);
			peano(i - 1, n, w, s, e);
			dirs.add(n);
			peano(i - 1, n, e, s, w);
			dirs.add(e);
			peano(i - 1, s, e, n, w);
			dirs.add(s);
			peano(i - 1, s, w, n, e);
			dirs.add(s);
			peano(i - 1, s, e, n, w);
			dirs.add(e);
			peano(i - 1, n, e, s, w);
			dirs.add(n);
			peano(i - 1, n, w, s, e);
			dirs.add(n);
			peano(i - 1, n, e, s, w);
		}
	}
}