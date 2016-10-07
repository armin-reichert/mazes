package de.amr.easy.grid.iterators.traversals;

import java.util.Iterator;

import de.amr.easy.grid.api.Grid2D;

/**
 * A grid traversal where the left and right grid "walls" are "collapsing".
 * 
 * @author Armin Reichert
 *
 * @param <Cell>
 *          the grid cell type
 */
public class CollapsingWalls<Cell> implements Sequence<Cell> {

	private final Grid2D<Cell, ?> grid;

	public CollapsingWalls(Grid2D<Cell, ?> grid) {
		this.grid = grid;
	}

	@Override
	public Iterator<Cell> iterator() {

		return new Iterator<Cell>() {

			private Cell nextLeft, nextRight;
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
				return visited < grid.vertexCount();
			}

			@Override
			public Cell next() {
				if (left) {
					Cell cell = nextLeft;
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
					Cell cell = nextRight;
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
