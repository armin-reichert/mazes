package de.amr.easy.grid.iterators.shapes;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

import de.amr.easy.grid.api.BareGrid2D;
import de.amr.easy.grid.api.Sequence;

/**
 * Base class for shapes (square, rectangle, ...) on a grid.
 * <p>
 * Implements the {@link Sequence} interface such that a shape can be used as an Iterator or a
 * Stream of cells.
 * 
 * @author Armin Reichert
 *
 */
public abstract class Shape implements Sequence<Integer> {

	public final BareGrid2D<?> grid;

	protected final List<Integer> cells = new ArrayList<>();

	protected Shape(BareGrid2D<?> grid) {
		this.grid = grid;
	}

	protected void addCell(int col, int row) {
		if (grid.isValidCol(col) && grid.isValidRow(row)) {
			cells.add(grid.cell(col, row));
		}
	}

	@Override
	public Iterator<Integer> iterator() {
		return cells.iterator();
	}

	@Override
	public Stream<Integer> stream() {
		return cells.stream();
	}
}