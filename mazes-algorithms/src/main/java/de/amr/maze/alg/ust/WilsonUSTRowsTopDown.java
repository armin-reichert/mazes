package de.amr.maze.alg.ust;

import static de.amr.graph.core.api.TraversalState.COMPLETED;
import static java.util.stream.IntStream.range;

import de.amr.graph.core.api.TraversalState;
import de.amr.graph.grid.api.GridGraph2D;

/**
 * Wilson's algorithm where the random walks start row-wise from top to bottom.
 * 
 * @author Armin Reichert
 */
public class WilsonUSTRowsTopDown extends WilsonUST {

	public WilsonUSTRowsTopDown(GridGraph2D<TraversalState, Integer> grid) {
		super(grid);
	}

	@Override
	public void createMaze(int x, int y) {
		grid.set(grid.cell(x, y), COMPLETED);
		range(0, grid.numRows()).forEach(row -> range(0, grid.numCols()).forEach(col -> {
			loopErasedRandomWalk(grid.cell(col, row));
		}));
	}
}