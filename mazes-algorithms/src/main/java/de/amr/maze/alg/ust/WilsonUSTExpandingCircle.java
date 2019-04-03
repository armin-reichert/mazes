package de.amr.maze.alg.ust;

import static de.amr.graph.grid.api.GridPosition.CENTER;
import static java.lang.Math.max;

import java.util.stream.IntStream;

import de.amr.graph.grid.traversals.ExpandingCircle;
import de.amr.maze.alg.core.OrthogonalGrid;

/**
 * Wilson's algorithm where the vertices are selected from an expanding circle.
 * 
 * @author Armin Reichert
 */
public class WilsonUSTExpandingCircle extends WilsonUST {

	public WilsonUSTExpandingCircle(int numCols, int numRows) {
		super(numCols, numRows);
	}

	@Override
	public OrthogonalGrid createMaze(int x, int y) {
		return runWilsonAlgorithm(grid.cell(CENTER));
	}

	@Override
	protected IntStream randomWalkStartCells() {
		return new ExpandingCircle(grid, grid.cell(CENTER), 1, max(grid.numCols(), grid.numRows())).stream();
	}
}