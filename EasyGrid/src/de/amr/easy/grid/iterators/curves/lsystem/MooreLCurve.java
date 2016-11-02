package de.amr.easy.grid.iterators.curves.lsystem;

import de.amr.easy.grid.iterators.curves.Curve;

/**
 * Computes a Moore curve from the following L-system:
 * <p>
 * <code>
 * S -> L f L + f + L f L <br/>
 * L -> - R f + L f L + f R - <br/>
 * R -> + L f - R f R - f L + </br>
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
public class MooreLCurve extends Curve {

	public MooreLCurve(int i) {
		S(i);
	}

	/**
	 * {@code S -> L f L + f + L f L}
	 * 
	 * @param n
	 *          recursion depth
	 */
	private void S(int n) {
		if (n > 0) {
			L(n - 1);
			forward();
			L(n - 1);
			left90();
			forward();
			left90();
			L(n - 1);
			forward();
			L(n - 1);
		}
	}

	/**
	 * {@code L -> - R f + L f L + f R -}
	 * 
	 * @param n
	 *          recursion depth
	 */
	private void L(int n) {
		if (n > 0) {
			right90();
			R(n - 1);
			forward();
			left90();
			L(n - 1);
			forward();
			L(n - 1);
			left90();
			forward();
			R(n - 1);
			right90();
		}
	}

	/**
	 * {@code R -> + L f - R f R - f L +}
	 * 
	 * @param n
	 *          recursion depth
	 */
	private void R(int n) {
		if (n > 0) {
			left90();
			L(n - 1);
			forward();
			right90();
			R(n - 1);
			forward();
			R(n - 1);
			right90();
			forward();
			L(n - 1);
			left90();
		}
	}
}