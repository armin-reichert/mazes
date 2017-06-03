package de.amr.easy.grid.curves;

import static de.amr.easy.grid.impl.Top4.Top4;
import static java.util.stream.Collectors.joining;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import de.amr.easy.grid.api.BareGrid2D;
import de.amr.easy.grid.api.CellSequence;
import de.amr.easy.grid.api.Topology;

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
		this(Top4);
	}

	@Override
	public Iterator<Integer> iterator() {
		return dirs.iterator();
	}

	@Override
	public String toString() {
		return stream().boxed().map(dir -> top.getName(dir)).collect(joining("-"));
	}

	public void traverse(BareGrid2D<?> grid, int start, BiConsumer<Integer, Integer> action) {
		int current = start;
		for (int dir : this) {
			int next = grid.neighbor(current, dir).getAsInt();
			action.accept(current, next);
			current = next;
		}
	}

	public IntStream cells(BareGrid2D<?> grid, int start) {
		List<Integer> cells = new ArrayList<>();
		int current = start;
		cells.add(current);
		for (int dir : this) {
			int next = grid.neighbor(current, dir).getAsInt();
			cells.add(next);
			current = next;
		}
		return cells.stream().mapToInt(Integer::intValue);
	}

	public String cellsAsString(BareGrid2D<?> grid, int start) {
		/*@formatter:off*/
		return cells(grid, start).boxed()
				.map(cell -> String.format("(%d,%d)", grid.col(cell), grid.row(cell)))
				.collect(Collectors.joining());
		/*@formatter:on*/
	}
}