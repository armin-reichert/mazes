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

	private void divide(int x, int y, int w, int h) {
		if (w <= 1 && h <= 1) {
			return;
		}
		boolean horizontalCut = (w < h) || (w == h && rnd.nextBoolean());
		if (horizontalCut) {
			// cut horizontally at random y-position from interval [y + 1, y + h - 1]
			int cutY = Math.min(grid.numRows() - 1, (y + 1) + rnd.nextInt(h - 1));
			int passage = rnd.nextInt(w);
			for (int i = 0; i < w; ++i) {
				if (i != passage && grid.isValidCol(x + i) && grid.isValidRow(cutY - 1)) {
					Integer u = grid.cell(x + i, cutY), v = grid.cell(x + i, cutY - 1);
					grid.edge(u, v).ifPresent(grid::removeEdge);
				}
			}
			divide(x, y, w, cutY - y);
			divide(x, cutY, w, h - cutY + y);
		} else {
			// cut vertically at random x-position from interval [x + 1, x + w - 1]
			int cutX = Math.min(grid.numCols() - 1, (x + 1) + rnd.nextInt(w - 1));
			int passage = rnd.nextInt(h);
			for (int j = 0; j < h; ++j) {
				if (j != passage && grid.isValidCol(cutX - 1) && grid.isValidRow(y + j)) {
					Integer u = grid.cell(cutX, y + j), v = grid.cell(cutX - 1, y + j);
					grid.edge(u, v).ifPresent(grid::removeEdge);
				}
			}
			divide(x, y, cutX - x, h);
			divide(cutX, y, w - cutX + x, h);
		}
	}
}