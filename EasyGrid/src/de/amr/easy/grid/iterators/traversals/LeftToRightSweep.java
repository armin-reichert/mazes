package de.amr.easy.grid.iterators.traversals;

import java.util.Iterator;

import de.amr.easy.grid.api.Grid2D;

/**
 * Traverses the grid by sweeping a vertical line from left to right.
 * 
 * @author Armin Reichert
 *
 * @param <Cell>
 *          the grid cell type
 */
public class LeftToRightSweep<Cell> implements Iterable<Cell> {

	private final Grid2D<Cell, ?> grid;

	public LeftToRightSweep(Grid2D<Cell, ?> grid) {
		this.grid = grid;
	}

	@Override
	public Iterator<Cell> iterator() {

		return new Iterator<Cell>() {

			private int x;
			private int y;

			@Override
			public boolean hasNext() {
				return !(x == grid.numCols() - 1 && y == grid.numRows() - 1);
			}

			@Override
			public Cell next() {
				if (y < grid.numRows() - 1) {
					++y;
				} else {
					++x;
					y = 0;
				}
				return grid.cell(x, y);
			}
		};
	}
}
