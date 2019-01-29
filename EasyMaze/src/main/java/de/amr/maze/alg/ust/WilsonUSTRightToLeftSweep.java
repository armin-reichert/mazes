package de.amr.maze.alg.ust;

import static de.amr.graph.grid.api.GridPosition.BOTTOM_RIGHT;

import java.util.stream.IntStream;

import de.amr.graph.grid.impl.OrthogonalGrid;
import de.amr.graph.grid.traversals.RightToLeftSweep;

/**
 * Wilson's algorithm where the vertices are selected column-wise left-to-right.
 * 
 * @author Armin Reichert
 */
public class WilsonUSTRightToLeftSweep extends WilsonUST {

	public WilsonUSTRightToLeftSweep(int numCols, int numRows) {
		super(numCols, numRows);
	}

	@Override
	public OrthogonalGrid createMaze(int x, int y) {
		return runWilsonAlgorithm(grid.cell(BOTTOM_RIGHT));
	}

	@Override
	protected IntStream randomWalkStartCells() {
		return new RightToLeftSweep(grid).stream();
	}
}