package de.amr.easy.maze.alg;

import static de.amr.easy.graph.api.TraversalState.COMPLETED;
import static java.lang.Math.min;
import static java.util.stream.IntStream.range;

import de.amr.easy.graph.api.SimpleEdge;
import de.amr.easy.graph.api.TraversalState;
import de.amr.easy.grid.api.Grid2D;

/**
 * Creates maze by recursive division.
 * 
 * @author Armin Reichert
 * 
 * @see <a href=
 *      "http://weblog.jamisbuck.org/2011/1/12/maze-generation-recursive-division-algorithm.html">Maze
 *      Generation: Recursive Division</a>
 */
public class RecursiveDivision extends MazeAlgorithm<SimpleEdge> {

	public RecursiveDivision(Grid2D<TraversalState, SimpleEdge> grid) {
		super(grid);
	}

	@Override
	public void run(int start) {
		grid.fill();
		grid.setDefaultContent(COMPLETED);
		divide(0, 0, grid.numCols(), grid.numRows());
	}

	/**
	 * Applies the division step to the sub-grid defined by the given left column, top row, width and
	 * height.
	 * 
	 * @param left
	 *          left column of sub-grid
	 * @param top
	 *          top row of sub-grid
	 * @param width
	 *          number of columns of sub-grid
	 * @param height
	 *          number of rows of sub-grid
	 */
	private void divide(int left, int top, int width, int height) {
		if (width <= 1 && height <= 1) {
			return;
		}
		boolean horizontalCut = (width < height) || (width == height && rnd.nextBoolean());
		if (horizontalCut) {
			// cut horizontally at random row from interval [top + 1, top + height - 1]
			int cut = min(grid.numRows() - 1, (top + 1) + rnd.nextInt(height - 1));
			int passage = rnd.nextInt(width);
			range(0, width).filter(col -> col != passage).forEach(col -> {
				if (grid.isValidCol(left + col) && grid.isValidRow(cut - 1)) {
					int u = grid.cell(left + col, cut), v = grid.cell(left + col, cut - 1);
					grid.edge(u, v).ifPresent(grid::removeEdge);
				}
			});
			divide(left, top, width, cut - top);
			divide(left, cut, width, height - cut + top);
		} else {
			// cut vertically at random column from interval [left + 1, left + width - 1]
			int cut = min(grid.numCols() - 1, (left + 1) + rnd.nextInt(width - 1));
			int passage = rnd.nextInt(height);
			range(0, height).filter(row -> row != passage).forEach(row -> {
				if (grid.isValidCol(cut - 1) && grid.isValidRow(top + row)) {
					int u = grid.cell(cut, top + row), v = grid.cell(cut - 1, top + row);
					grid.edge(u, v).ifPresent(grid::removeEdge);
				}
			});
			divide(left, top, cut - left, height);
			divide(cut, top, width - cut + left, height);
		}
	}
}