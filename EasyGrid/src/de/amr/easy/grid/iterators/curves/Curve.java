package de.amr.easy.grid.iterators.curves;

import static java.util.stream.Collectors.joining;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

import de.amr.easy.grid.iterators.Sequence;

/**
 * Base class for curves like the Hilbert curve.
 * 
 * @author Armin Reichert
 * 
 * @param D
 *          direction type
 */
public abstract class Curve<D> implements Sequence<D> {

	protected final List<D> curve = new ArrayList<>();
	protected final Compas<D> compas;

	protected Curve() {
		this(null);
	}

	protected Curve(Compas<D> compas) {
		this.compas = compas;
	}

	protected void walk(D dir) {
		curve.add(dir);
	}

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
	public Iterator<D> iterator() {
		return curve.iterator();
	}

	@Override
	public Stream<D> stream() {
		return curve.stream();
	}

	@Override
	public String toString() {
		return stream().map(D::toString).collect(joining("-"));
	}
}