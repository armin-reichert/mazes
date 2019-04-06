package de.amr.maze.alg.ust;

import static de.amr.graph.grid.api.GridPosition.BOTTOM_RIGHT;

import java.util.stream.IntStream;

import de.amr.graph.core.api.TraversalState;
import de.amr.graph.grid.api.GridGraph2D;
import de.amr.graph.grid.traversals.RightToLeftSweep;
import de.amr.maze.alg.core.MazeGridFactory;

/**
 * Wilson's algorithm where the vertices are selected column-wise left-to-right.
 * 
 * @author Armin Reichert
 */
public class WilsonUSTRightToLeftSweep extends WilsonUST {

	public WilsonUSTRightToLeftSweep(MazeGridFactory factory, int numCols, int numRows) {
		super(factory, numCols, numRows);
	}

	@Override
	public GridGraph2D<TraversalState, Integer> createMaze(int x, int y) {
		return runWilsonAlgorithm(grid.cell(BOTTOM_RIGHT));
	}

	@Override
	protected IntStream randomWalkStartCells() {
		return new RightToLeftSweep(grid).stream();
	}
}