package de.amr.easy.maze.alg.ust;

import static de.amr.easy.grid.api.GridPosition.CENTER;

import java.util.stream.IntStream;

import de.amr.easy.grid.iterators.traversals.Spiral;
import de.amr.easy.maze.alg.core.OrthogonalGrid;

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
		return runWilsonAlgorithm(maze, maze.cell(CENTER));
	}

	@Override
	protected IntStream randomWalkStartCells(OrthogonalGrid maze) {
		return new Spiral(maze, maze.cell(CENTER)).stream();
	}
}