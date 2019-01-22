package de.amr.easy.maze.alg.ust;

import static de.amr.easy.graph.grid.api.GridPosition.CENTER;

import java.util.stream.IntStream;

import de.amr.easy.graph.grid.impl.OrthogonalGrid;
import de.amr.easy.graph.grid.traversals.Spiral;

/**
 * Wilson's algorithm where the vertices are selected from an expanding spiral.
 * 
 * @author Armin Reichert
 */
public class WilsonUSTExpandingSpiral extends WilsonUST {

	public WilsonUSTExpandingSpiral(int numCols, int numRows) {
		super(numCols, numRows);
	}

	@Override
	public OrthogonalGrid createMaze(int x, int y) {
		return runWilsonAlgorithm(grid.cell(CENTER));
	}

	@Override
	protected IntStream randomWalkStartCells() {
		return new Spiral(grid, grid.cell(CENTER)).stream();
	}
}