package de.amr.easy.grid.iterators.shapes;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

import de.amr.easy.grid.api.Grid2D;

/**
 * Base class for shapes (square, rectangle, ...) on a grid.
 * <p>
 * Implements the {@link Iterable} interface such that code like the following can be written:
 * 
 * <pre>
 * Rectangle&lt;Integer> rect = new Rectangle<>(grid, grid.getCell(0, 0), 10, 5);
 * for (Integer cell : rect) {
 * 	// process cell
 * }
 * </pre>
 * 
 * @author Armin Reichert
 *
 * @param <Cell>
 *          the grid cell type
 */
public abstract class Shape<Cell> implements Iterable<Cell> {

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

	public Stream<Cell> stream() {
		return cells.stream();
	}
}