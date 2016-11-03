package de.amr.easy.grid.iterators.curves;

import static java.util.stream.Collectors.joining;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

import de.amr.easy.grid.api.Direction;
import de.amr.easy.grid.iterators.Sequence;

/**
 * Base class for curves like the Hilbert curve.
 * 
 * @author Armin Reichert
 */
public abstract class Curve implements Sequence<Direction> {

	protected final List<Direction> curve = new ArrayList<>();

	private Compas compas = new Compas();

	protected void forward() {
		curve.add(compas.ahead());
	}

	protected void left90() {
		compas.turnLeft();
	}

	protected void right90() {
		compas.turnRight();
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
		return stream().map(Direction::toString).collect(joining("-"));
	}
}