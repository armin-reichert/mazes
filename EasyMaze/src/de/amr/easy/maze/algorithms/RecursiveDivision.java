package de.amr.easy.maze.algorithms;

import static de.amr.easy.graph.api.TraversalState.COMPLETED;

import java.util.Random;
import java.util.function.Consumer;

import de.amr.easy.graph.api.TraversalState;
import de.amr.easy.grid.api.ObservableDataGrid2D;

/**
 * Creates maze by recursive division.
 * 
 * @author Armin Reichert
 * 
 * @see <a href=
 *      "http://weblog.jamisbuck.org/2011/1/12/maze-generation-recursive-division-algorithm.html">The
 *      Buckblog</a>
 */
public class RecursiveDivision implements Consumer<Integer> {

	private final ObservableDataGrid2D<TraversalState> grid;
	private final Random rnd = new Random();

	public RecursiveDivision(ObservableDataGrid2D<TraversalState> grid) {
		this.grid = grid;
		grid.setEventsEnabled(false);
		grid.makeFullGrid();
		grid.setEventsEnabled(true);
		grid.setDefault(COMPLETED);
	}

	@Override
	public void accept(Integer start) {
		divide(0, 0, grid.numCols(), grid.numRows());
	}

	private void divide(int leftUpperCol, int leftUpperRow, int w, int h) {
		if (w <= 1 && h <= 1) {
			return;
		}
		boolean horizontalCut = (w < h) || (w == h && rnd.nextBoolean());
		if (horizontalCut) {
			// cut horizontally at random row from interval [leftUpperRow + 1, leftUpperRow + h - 1]
			int cutRow = Math.min(grid.numRows() - 1, (leftUpperRow + 1) + rnd.nextInt(h - 1));
			int passageCol = rnd.nextInt(w);
			for (int col = 0; col < w; ++col) {
				if (col != passageCol && grid.isValidCol(leftUpperCol + col) && grid.isValidRow(cutRow - 1)) {
					Integer u = grid.cell(leftUpperCol + col, cutRow), v = grid.cell(leftUpperCol + col, cutRow - 1);
					grid.edge(u, v).ifPresent(grid::removeEdge);
				}
			}
			divide(leftUpperCol, leftUpperRow, w, cutRow - leftUpperRow);
			divide(leftUpperCol, cutRow, w, h - cutRow + leftUpperRow);
		} else {
			// cut vertically at random col from interval [leftUpperCol + 1, leftUpperCol + w - 1]
			int cutCol = Math.min(grid.numCols() - 1, (leftUpperCol + 1) + rnd.nextInt(w - 1));
			int passageRow = rnd.nextInt(h);
			for (int row = 0; row < h; ++row) {
				if (row != passageRow && grid.isValidCol(cutCol - 1) && grid.isValidRow(leftUpperRow + row)) {
					Integer u = grid.cell(cutCol, leftUpperRow + row), v = grid.cell(cutCol - 1, leftUpperRow + row);
					grid.edge(u, v).ifPresent(grid::removeEdge);
				}
			}
			divide(leftUpperCol, leftUpperRow, cutCol - leftUpperCol, h);
			divide(cutCol, leftUpperRow, w - cutCol + leftUpperCol, h);
		}
	}
}