package de.amr.easy.grid.curves;

import java.util.ArrayList;
import java.util.List;

import de.amr.easy.grid.api.Topology;
import de.amr.easy.grid.impl.Topologies;

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
 * <li><code>f</code> = go forward
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
public class MooreLCurve implements Curve {

	private final List<Integer> dirs = new ArrayList<>();

	private final Compass4 compass = new Compass4();

	// non-terminal symbol interpretations:

	private void minus() {
		compass.turnRight();
	}

	private void plus() {
		compass.turnLeft();
	}

	private void f() {
		dirs.add(compass.ahead());
	}

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
			f();
			L(i - 1);
			plus();
			f();
			plus();
			L(i - 1);
			f();
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
			minus();
			R(i - 1);
			f();
			plus();
			L(i - 1);
			f();
			L(i - 1);
			plus();
			f();
			R(i - 1);
			minus();
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
			plus();
			L(i - 1);
			f();
			minus();
			R(i - 1);
			f();
			R(i - 1);
			minus();
			f();
			L(i - 1);
			plus();
		}
	}

	@Override
	public Topology getTopology() {
		return Topologies.TOP4;
	}

	@Override
	public Iterable<Integer> dirs() {
		return dirs;
	}

}