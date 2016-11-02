package de.amr.easy.grid.iterators.curves;

/**
 * Computes a Hilbert-Moore curve as a list of directions. This curve must start at grid position
 * (nCols / 2 - 1, nRows - 1) where nCols = nRows = power of 2.
 *
 * @author Armin Reichert
 * 
 * @see http://cph.phys.spbu.ru/ACOPhys/materials/bader/sfc.pdf
 * @see https://en.wikipedia.org/wiki/Moore_curve
 */
public class MooreCurve extends Curve {
	
	public MooreCurve(int i) {
		start(i);
	}

	/**
	 * Corresponds to the start rule (axiom) of the Lindenmayer-system.
	 * <p>
	 * {@code S -> L f L + f + L f L}
	 */
	protected void start(int n) {
		if (n > 0) {
			L(n - 1);
			forward();
			L(n - 1);
			left();
			forward();
			left();
			L(n - 1);
			forward();
			L(n - 1);
		}
	}

	/**
	 * Corresponds to the single L-rule of the Lindenmayer-system.
	 * <p>
	 * {@code L -> - R f + L f L + f R -}
	 */
	private void L(int n) {
		if (n > 0) {
			right();
			R(n - 1);
			forward();
			left();
			L(n - 1);
			forward();
			L(n - 1);
			left();
			forward();
			R(n - 1);
			right();
		}
	}

	/**
	 * Corresponds to the single R-rule of the Lindenmayer-system.
	 * <p>
	 * {@code R -> + L f - R f R - f L +}
	 */
	private void R(int n) {
		if (n > 0) {
			left();
			L(n - 1);
			forward();
			right();
			R(n - 1);
			forward();
			R(n - 1);
			right();
			forward();
			L(n - 1);
			left();
		}
	}
}