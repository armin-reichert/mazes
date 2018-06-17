package de.amr.easy.grid.iterators.traversals;

import java.util.Iterator;

import de.amr.easy.grid.api.BareGrid2D;
import de.amr.easy.grid.api.CellSequence;

/**
 * A sequence of cells traversing the grid like "walls" which are growing horizontally from the
 * sides towards the center.
 * 
 * @author Armin Reichert
 */
public class CollapsingWalls implements CellSequence {

	private final BareGrid2D<?> grid;

	public CollapsingWalls(BareGrid2D<?> grid) {
		this.grid = grid;
	}

	@Override
	public Iterator<Integer> iterator() {

		return new Iterator<Integer>() {

			private int nextLeft, nextRight;
			private boolean left;
			private int visited;

			{
				left = true;
				nextLeft = grid.cell(0, 0);
				nextRight = grid.cell(grid.numCols() - 1, grid.numRows() - 1);
				visited = 0;
			}

			@Override
			public boolean hasNext() {
				return visited < grid.numVertices();
			}

			@Override
			public Integer next() {
				if (left) {
					int cell = nextLeft;
					int x = grid.col(nextLeft), y = grid.row(nextLeft);
					if (y < grid.numRows() - 1) {
						nextLeft = grid.cell(x, y + 1);
					} else {
						nextLeft = grid.cell(x + 1, 0);
					}
					left = false;
					++visited;
					return cell;
				} else {
					int cell = nextRight;
					int x = grid.col(nextRight), y = grid.row(nextRight);
					if (y > 0) {
						nextRight = grid.cell(x, y - 1);
					} else {
						nextRight = grid.cell(x - 1, grid.numRows() - 1);
					}
					left = true;
					++visited;
					return cell;
				}
			}
		};
	}
}