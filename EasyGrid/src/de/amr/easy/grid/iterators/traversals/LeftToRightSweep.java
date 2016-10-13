package de.amr.easy.grid.iterators.traversals;

import java.util.Iterator;

import de.amr.easy.grid.api.Grid2D;

/**
 * Traverses the grid by sweeping a vertical line from left to right.
 * 
 * @author Armin Reichert
 *
 * @param <Integer>
 *          the grid cell type
 */
public class LeftToRightSweep implements Sequence<Integer> {

	private final Grid2D grid;

	public LeftToRightSweep(Grid2D grid) {
		this.grid = grid;
	}

	@Override
	public Iterator<Integer> iterator() {

		return new Iterator<Integer>() {

			private int x;
			private int y;

			@Override
			public boolean hasNext() {
				return !(x == grid.numCols() - 1 && y == grid.numRows() - 1);
			}

			@Override
			public Integer next() {
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
