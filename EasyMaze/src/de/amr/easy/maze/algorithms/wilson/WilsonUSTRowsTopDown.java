package de.amr.easy.maze.algorithms.wilson;

import java.util.stream.IntStream;

import de.amr.easy.graph.api.TraversalState;
import de.amr.easy.grid.api.ObservableDataGrid2D;

/**
 * Wilson's algorithm where the vertices are selected row-wise.
 * 
 * @author Armin Reichert
 */
public class WilsonUSTRowsTopDown extends WilsonUST {

	public WilsonUSTRowsTopDown(ObservableDataGrid2D<Integer, TraversalState> grid) {
		super(grid);
	}

	@Override
	public void accept(Integer start) {
		addCellToTree(start);
		IntStream.range(0, grid.numRows()).forEach(row -> {
			IntStream.range(0, grid.numCols()).forEach(col -> {
				Integer walkStart = grid.cell(col, row);
				if (!isCellInTree(walkStart)) {
					loopErasedRandomWalk(walkStart);
				}
			});
		});
	}
}