package de.amr.easy.grid.iterators.shapes;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

import de.amr.easy.grid.api.Grid2D;
import de.amr.easy.grid.iterators.traversals.Sequence;

/**
 * Base class for shapes (square, rectangle, ...) on a grid.
 * <p>
 * Implements the {@link Sequence} interface such that a shape can be used as an Iterator or a
 * Stream of cells.
 * 
 * @author Armin Reichert
 *
 * @param <Cell>
 *          the grid cell type
 */
public abstract class Shape<Cell> implements Sequence<Cell> {

	public final Grid2D<Cell, ?> grid;

	protected final List<Cell> cells = new ArrayList<>();

	protected Shape(Grid2D<Cell, ?> grid) {
		this.grid = grid;
	}

	protected void addCell(int col, int row) {
		if (grid.isValidCol(col) && grid.isValidRow(row)) {
			cells.add(grid.cell(col, row));
		}
	}

	@Override
	public Iterator<Cell> iterator() {
		return cells.iterator();
	}

	@Override
	public Stream<Cell> stream() {
		return cells.stream();
	}
}