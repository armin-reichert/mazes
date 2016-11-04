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