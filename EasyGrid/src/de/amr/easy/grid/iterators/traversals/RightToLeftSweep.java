package de.amr.easy.grid.iterators.traversals;

import java.util.Iterator;

import de.amr.easy.grid.api.Grid2D;

/**
 * Traverses the grid by sweeping a vertical line from right to left.
 * 
 * @author Armin Reichert
 *
 * @param <Cell>
 *          the grid cell type
 */
public class RightToLeftSweep<Cell> implements Sequence<Cell> {

	private final Grid2D<Cell, ?> grid;

	public RightToLeftSweep(Grid2D<Cell, ?> grid) {
		this.grid = grid;
	}

	@Override
	public Iterator<Cell> iterator() {
		return new Iterator<Cell>() {

			private int x = grid.numCols() - 1;
			private int y = grid.numRows() - 1;

			@Override
			public boolean hasNext() {
				return !(x == 0 && y == 0);
			}

			@Override
			public Cell next() {
				if (y > 0) {
					--y;
				} else {
					--x;
					y = grid.numRows() - 1;
				}
				return grid.cell(x, y);
			}
		};
	}
}