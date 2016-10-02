package de.amr.easy.grid.iterators.shapes;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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

	protected final Grid2D<Cell, ?> grid;
	protected final List<Cell> cells;

	protected Shape(Grid2D<Cell, ?> grid) {
		this.grid = grid;
		cells = new ArrayList<Cell>();
	}

	protected void addCell(int x, int y) {
		if (grid.isValidCol(x) && grid.isValidRow(y)) {
			cells.add(grid.cell(x, y));
		}
	}

	public Grid2D<Cell, ?> getGrid() {
		return grid;
	}

	@Override
	public Iterator<Cell> iterator() {
		return cells.iterator();
	}
}
