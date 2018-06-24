package de.amr.easy.maze.alg.ust;

import static de.amr.easy.graph.api.traversal.TraversalState.COMPLETED;
import static java.util.stream.IntStream.range;

import de.amr.easy.maze.alg.core.OrthogonalGrid;

/**
 * Wilson's algorithm where the random walks start row-wise from top to bottom.
 * 
 * @author Armin Reichert
 */
public class WilsonUSTRowsTopDown extends WilsonUST {

	public WilsonUSTRowsTopDown(int numCols, int numRows) {
		super(numCols, numRows);
	}

	@Override
	public OrthogonalGrid createMaze(int x, int y) {
		maze.set(maze.cell(x, y), COMPLETED);
		range(0, maze.numRows()).forEach(row -> range(0, maze.numCols()).forEach(col -> {
			loopErasedRandomWalk(maze.cell(col, row));
		}));
		return maze;
	}
}