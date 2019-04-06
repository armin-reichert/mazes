package de.amr.maze.alg.ust;

import static de.amr.graph.grid.api.GridPosition.CENTER;

import java.util.stream.IntStream;

import de.amr.graph.core.api.TraversalState;
import de.amr.graph.grid.api.GridGraph2D;
import de.amr.graph.grid.traversals.Spiral;
import de.amr.maze.alg.core.MazeGridFactory;

/**
 * Wilson's algorithm where the vertices are selected from an expanding spiral.
 * 
 * @author Armin Reichert
 */
public class WilsonUSTExpandingSpiral extends WilsonUST {

	public WilsonUSTExpandingSpiral(MazeGridFactory factory, int numCols, int numRows) {
		super(factory, numCols, numRows);
	}

	@Override
	public GridGraph2D<TraversalState, Integer> createMaze(int x, int y) {
		return runWilsonAlgorithm(grid.cell(CENTER));
	}

	@Override
	protected IntStream randomWalkStartCells() {
		return new Spiral(grid, grid.cell(CENTER)).stream();
	}
}