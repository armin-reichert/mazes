package de.amr.easy.grid.iterators.traversals;

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

	public MooreCurve(int depth) {
		produce(depth, Direction.values());
	}

	private Direction[] turnLeft(Direction[] dirs) {
		return Stream.of(dirs).map(Direction::left).toArray(Direction[]::new);
	}

	private Direction[] turnRight(Direction[] dirs) {
		return Stream.of(dirs).map(Direction::right).toArray(Direction[]::new);
	}
	
	private void forward(Direction[] dirs) {
		curve.add(dirs[0]);
	}

	private void produce(int n, Direction[] dirs) {
		axiom(n, dirs);
	}

	/**
	 * Corresponds to the axiom of the Lindenmayer-system.
	 */
	private void axiom(int n, Direction[] dirs) {
		if (n > 0) {
			L(n - 1, dirs);
			forward(dirs);
			L(n - 1, dirs);
			dirs = turnLeft(dirs);
			forward(dirs);
			dirs = turnLeft(dirs);
			L(n - 1, dirs);
			forward(dirs);
			L(n - 1, dirs);
		}
	}

	/**
	 * Corresponds to the single L-rule of the Lindenmayer-system.
	 */
	private void L(int n, Direction[] dirs) {
		if (n > 0) {
			dirs = turnRight(dirs);
			R(n - 1, dirs);
			forward(dirs);
			dirs = turnLeft(dirs);
			L(n - 1, dirs);
			forward(dirs);
			L(n - 1, dirs);
			dirs = turnLeft(dirs);
			forward(dirs);
			R(n - 1, dirs);
			dirs = turnRight(dirs);
		}
	}

	/**
	 * Corresponds to the single R-rule of the Lindenmayer-system.
	 */
	private void R(int n, Direction[] dirs) {
		if (n > 0) {
			dirs = turnLeft(dirs);
			L(n - 1, dirs);
			forward(dirs);
			dirs = turnRight(dirs);
			R(n - 1, dirs);
			forward(dirs);
			R(n - 1, dirs);
			dirs = turnRight(dirs);
			forward(dirs);
			L(n - 1, dirs);
			dirs = turnLeft(dirs);
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