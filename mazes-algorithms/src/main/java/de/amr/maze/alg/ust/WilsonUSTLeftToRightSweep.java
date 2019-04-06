package de.amr.maze.alg.ust;

import static de.amr.graph.grid.api.GridPosition.TOP_LEFT;

import java.util.stream.IntStream;

import de.amr.graph.core.api.TraversalState;
import de.amr.graph.grid.api.GridGraph2D;
import de.amr.graph.grid.traversals.LeftToRightSweep;
import de.amr.maze.alg.core.MazeGridFactory;

/**
 * Wilson's algorithm where the vertices are selected column-wise left-to-right.
 * 
 * @author Armin Reichert
 */
public class WilsonUSTLeftToRightSweep extends WilsonUST {

	public WilsonUSTLeftToRightSweep(MazeGridFactory factory, int numCols, int numRows) {
		super(factory, numCols, numRows);
	}

	@Override
	public GridGraph2D<TraversalState, Integer> createMaze(int x, int y) {
		return runWilsonAlgorithm(grid.cell(TOP_LEFT));
	}

	@Override
	protected IntStream randomWalkStartCells() {
		return new LeftToRightSweep(grid).stream();
	}
}