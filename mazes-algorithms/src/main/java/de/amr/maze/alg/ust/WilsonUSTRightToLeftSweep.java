package de.amr.maze.alg.ust;

import static de.amr.graph.grid.api.GridPosition.BOTTOM_RIGHT;

import java.util.stream.IntStream;

import de.amr.graph.core.api.TraversalState;
import de.amr.graph.grid.api.GridGraph2D;
import de.amr.graph.grid.traversals.RightToLeftSweep;

/**
 * Wilson's algorithm where the vertices are selected column-wise left-to-right.
 * 
 * @author Armin Reichert
 */
public class WilsonUSTRightToLeftSweep extends WilsonUST {

	public WilsonUSTRightToLeftSweep(GridGraph2D<TraversalState, Integer> grid) {
		super(grid);
	}

	@Override
	public void createMaze(int x, int y) {
		runWilsonAlgorithm(grid.cell(BOTTOM_RIGHT));
	}

	@Override
	protected IntStream randomWalkStartCells() {
		return new RightToLeftSweep(grid).stream();
	}
}