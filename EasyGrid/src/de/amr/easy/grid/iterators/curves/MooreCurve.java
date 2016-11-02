package de.amr.easy.grid.iterators.curves;

/**
 * Computes a Moore curve from the following L-system:
 * <p>
 * <code>
 * r1: S -> L f L + f + L f L <br/>
 * r2: L -> - R f + L f L + f R - <br/>
 * r3: R -> + L f - R f R - f L + </br>
 * </code>
 * <p>
 * with non-terminals <code>{S, L, R}</code> and terminals <code>{f, +, -}</code>.
 * <p>
 * The terminals are interpreted as follows:
 * <ul>
 * <li>"f" = forward
 * <li>"+" = turn 90&deg; counterclockwise
 * <li>"-" = turn 90&deg; clockwise.
 * </ul>
 * <p>
 * On a <code>(n x n)</code>-grid, the curve starts at <code>column = n / 2 - 1, row = n - 1</code>
 * where <code>n = 2^k</code>.
 *
 * @author Armin Reichert
 * 
 * @see http://cph.phys.spbu.ru/ACOPhys/materials/bader/sfc.pdf
 * @see https://en.wikipedia.org/wiki/Moore_curve
 */
public class MooreCurve extends Curve {
	
	public MooreCurve(int i) {
		r1(i);
	}

	/**
	 * {@code S -> L f L + f + L f L}
	 * 
	 * @param n
	 *          recursion depth
	 */
	private void r1(int n) {
		/*@formatter:off*/
		if (n > 0) { r2(n - 1);forward();r2(n - 1);left();forward();left();r2(n - 1);forward();r2(n - 1); }
		/*@formatter:on*/
	}

	/**
	 * {@code L -> - R f + L f L + f R -}
	 * 
	 * @param n
	 *          recursion depth
	 */
	private void r2(int n) {
		/*@formatter:off*/
		if (n > 0) { right();r3(n - 1);forward();left();r2(n - 1);forward();r2(n - 1);left();forward();r3(n - 1);right();	}
		/*@formatter:on*/
	}

	/**
	 * {@code R -> + L f - R f R - f L +}
	 * 
	 * @param n
	 *          recursion depth
	 */
	private void r3(int n) {
		/*@formatter:off*/
		if (n > 0) { left();r2(n - 1);forward();right();r3(n - 1);forward();r3(n - 1);right();forward();r2(n - 1);left(); }
		/*@formatter:on*/
	}
}