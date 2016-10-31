package de.amr.easy.grid.iterators.curves;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import de.amr.easy.grid.api.Direction;
import de.amr.easy.grid.iterators.Sequence;

/**
 * Computes a Hilbert-Moore curve as a list of directions. This curve must start at grid position
 * (nCols / 2 - 1, nRows - 1) where nCols = nRows = power of 2.
 *
 * @author Armin Reichert
 * 
 * @see http://cph.phys.spbu.ru/ACOPhys/materials/bader/sfc.pdf
 * @see https://en.wikipedia.org/wiki/Moore_curve
 */
public class MooreCurve implements Sequence<Direction> {

	private final List<Direction> curve = new ArrayList<>();
	private Compas compas = new Compas(); // aka "turtle"

	private void forward() {
		curve.add(compas.ahead());
	}

	private void left() {
		compas = compas.turnLeft();
	}

	private void right() {
		compas = compas.turnRight();
	}

	public MooreCurve(int depth) {
		start(depth);
	}

	/**
	 * Corresponds to the start rule (axiom) of the Lindenmayer-system.
	 * <p>
	 * {@code S -> L f L + f + L f L}
	 */
	private void start(int n) {
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

	@Override
	public Iterator<Direction> iterator() {
		return curve.iterator();
	}

	@Override
	public Stream<Direction> stream() {
		return curve.stream();
	}

	@Override
	public String toString() {
		return curve.stream().map(Direction::toString).collect(Collectors.joining("-"));
	}
}