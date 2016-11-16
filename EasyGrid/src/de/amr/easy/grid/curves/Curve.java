package de.amr.easy.grid.curves;

import static java.util.stream.Collectors.joining;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import de.amr.easy.grid.api.CellSequence;
import de.amr.easy.grid.api.Topology;
import de.amr.easy.grid.impl.Top4;

/**
 * Base class for curves like the Hilbert curve.
 * 
 * @author Armin Reichert
 */
public abstract class Curve implements CellSequence {

	protected final Topology top;
	protected final List<Integer> dirs = new ArrayList<>();

	public Curve(Topology top) {
		this.top = top;
	}

	public Curve() {
		this(new Top4());
	}

	@Override
	public Iterator<Integer> iterator() {
		return dirs.iterator();
	}

	@Override
	public String toString() {
		return stream().boxed().map(dir -> top.getName(dir)).collect(joining("-"));
	}
}