package de.amr.easy.grid.iterators.curves.lsystem;

import de.amr.easy.grid.api.Dir4;
import de.amr.easy.grid.iterators.curves.Compas4;
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
 * with nonterminals <code>{S, L, R}</code>, axiom <code>S</code> and terminals
 * <code>{f, +, -}</code>.
 * <p>
 * The terminals are interpreted as follows:
 * <ul>
 * <li><code>f</code> = forward
 * <li><code>+</code> = turn 90&deg; counter-clockwise
 * <li><code>-</code> = turn 90&deg; clockwise.
 * </ul>
 * <p>
 * On a <code>(n x n)</code>-grid, the curve starts at <code>column = n / 2 - 1, row = n - 1</code>
 * where <code>n</code> has to be a power of 2.
 *
 * @author Armin Reichert
 * 
 * @see http://cph.phys.spbu.ru/ACOPhys/materials/bader/sfc.pdf
 * @see https://en.wikipedia.org/wiki/Moore_curve
 */
public class MooreLCurve extends Curve<Dir4> {

	private final Compas4 compas = new Compas4();

	public MooreLCurve(int i) {
		S(i);
	}

	/**
	 * {@code S -> L f L + f + L f L}
	 * 
	 * @param i
	 *          the recursion depth
	 */
	private void S(int i) {
		if (i > 0) {
			L(i - 1);
			curve.add(compas.ahead());
			L(i - 1);
			compas.turnLeft();
			curve.add(compas.ahead());
			compas.turnLeft();
			L(i - 1);
			curve.add(compas.ahead());
			L(i - 1);
		}
	}

	/**
	 * {@code L -> - R f + L f L + f R -}
	 * 
	 * @param i
	 *          the recursion depth
	 */
	private void L(int i) {
		if (i > 0) {
			compas.turnRight();
			R(i - 1);
			curve.add(compas.ahead());
			compas.turnLeft();
			L(i - 1);
			curve.add(compas.ahead());
			L(i - 1);
			compas.turnLeft();
			curve.add(compas.ahead());
			R(i - 1);
			compas.turnRight();
		}
	}

	/**
	 * {@code R -> + L f - R f R - f L +}
	 * 
	 * @param i
	 *          the recursion depth
	 */
	private void R(int i) {
		if (i > 0) {
			compas.turnLeft();
			L(i - 1);
			curve.add(compas.ahead());
			compas.turnRight();
			R(i - 1);
			curve.add(compas.ahead());
			R(i - 1);
			compas.turnRight();
			curve.add(compas.ahead());
			L(i - 1);
			compas.turnLeft();
		}
	}
}