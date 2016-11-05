package de.amr.easy.grid.curves;

import static java.util.stream.Collectors.joining;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

import de.amr.easy.grid.api.Sequence;

/**
 * Base class for curves like the Hilbert curve.
 * 
 * @author Armin Reichert
 * 
 * @param Dir
 *          direction type
 */
public abstract class Curve<Dir> implements Sequence<Dir> {

	protected final List<Dir> curve = new ArrayList<>();

	@Override
	public Iterator<Dir> iterator() {
		return curve.iterator();
	}

	@Override
	public Stream<Dir> stream() {
		return curve.stream();
	}

	@Override
	public String toString() {
		return stream().map(Dir::toString).collect(joining("-"));
	}
}