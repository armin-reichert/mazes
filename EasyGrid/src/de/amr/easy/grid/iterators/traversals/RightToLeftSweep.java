package de.amr.easy.grid.iterators.traversals;

import java.util.Iterator;

import de.amr.easy.grid.api.CellSequence;
import de.amr.easy.grid.api.GridGraph2D;

/**
 * A sequence of cells filling the grid by sweeping a vertical line from right to left.
 * 
 * @author Armin Reichert
 */
public class RightToLeftSweep implements CellSequence {

	private final GridGraph2D<?, ?> grid;

	public RightToLeftSweep(GridGraph2D<?, ?> grid) {
		this.grid = grid;
	}

	@Override
	public Iterator<Integer> iterator() {
		return new Iterator<Integer>() {

			private int x = grid.numCols() - 1;
			private int y = grid.numRows() - 1;

			@Override
			public boolean hasNext() {
				return !(x == 0 && y == 0);
			}

			@Override
			public Integer next() {
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