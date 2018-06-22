package de.amr.easy.maze.alg.ust;

import static de.amr.easy.graph.api.traversal.TraversalState.COMPLETED;
import static java.util.stream.IntStream.range;

import de.amr.easy.maze.alg.core.OrthogonalGrid;

/**
 * Wilson's algorithm where the vertices are selected row-wise.
 * 
 * @author Armin Reichert
 */
public class WilsonUSTRowsTopDown extends WilsonUST {

	public WilsonUSTRowsTopDown(OrthogonalGrid grid) {
		super(grid);
	}

	@Override
	public void run(int start) {
		grid.set(start, COMPLETED);
		range(0, grid.numRows()).forEach(row -> {
			range(0, grid.numCols()).forEach(col -> {
				loopErasedRandomWalk(grid.cell(col, row));
			});
		});
	}
}