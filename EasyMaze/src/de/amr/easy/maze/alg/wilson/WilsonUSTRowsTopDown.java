package de.amr.easy.maze.alg.wilson;

import static java.util.stream.IntStream.range;

import de.amr.easy.graph.api.TraversalState;
import de.amr.easy.grid.api.Grid2D;

/**
 * Wilson's algorithm where the vertices are selected row-wise.
 * 
 * @author Armin Reichert
 */
public class WilsonUSTRowsTopDown extends WilsonUST {

	public WilsonUSTRowsTopDown(Grid2D<TraversalState> grid) {
		super(grid);
	}

	@Override
	public void accept(Integer start) {
		addCellToTree(start);
		range(0, grid.numRows()).forEach(row -> {
			range(0, grid.numCols()).forEach(col -> {
				Integer walkStart = grid.cell(col, row);
				if (isOutsideTree(walkStart)) {
					loopErasedRandomWalk(walkStart);
				}
			});
		});
	}
}