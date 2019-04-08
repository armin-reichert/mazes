package de.amr.maze.alg.ust;

import static de.amr.graph.grid.api.GridPosition.CENTER;
import static java.lang.Math.max;

import java.util.stream.IntStream;

import de.amr.graph.core.api.TraversalState;
import de.amr.graph.grid.api.GridGraph2D;
import de.amr.graph.grid.traversals.ExpandingCircle;

/**
 * Wilson's algorithm where the vertices are selected from an expanding circle.
 * 
 * @author Armin Reichert
 */
public class WilsonUSTExpandingCircle extends WilsonUST {

	public WilsonUSTExpandingCircle(GridGraph2D<TraversalState, Integer> grid) {
		super(grid);
	}

	@Override
	public void createMaze(int x, int y) {
		runWilsonAlgorithm(grid.cell(CENTER));
	}

	@Override
	protected IntStream randomWalkStartCells() {
		return new ExpandingCircle(grid, grid.cell(CENTER), 1, max(grid.numCols(), grid.numRows())).stream();
	}
}