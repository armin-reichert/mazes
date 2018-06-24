package de.amr.easy.maze.alg.ust;

import static de.amr.easy.grid.api.GridPosition.TOP_LEFT;

import java.util.stream.IntStream;

import de.amr.easy.grid.iterators.shapes.Rectangle;
import de.amr.easy.grid.iterators.traversals.ExpandingRectangle;
import de.amr.easy.maze.alg.core.OrthogonalGrid;

/**
 * Wilson's algorithm where the vertices are selected from an expanding rectangle.
 * 
 * @author Armin Reichert
 */
public class WilsonUSTExpandingRectangle extends WilsonUST {

	public WilsonUSTExpandingRectangle(int numCols, int numRows) {
		super(numCols, numRows);
	}

	@Override
	public OrthogonalGrid createMaze(int x, int y) {
		return runWilsonAlgorithm(maze, maze.cell(TOP_LEFT));
	}

	@Override
	protected IntStream randomWalkStartCells(OrthogonalGrid maze) {
		Rectangle startRect = new Rectangle(maze, maze.cell(TOP_LEFT), 1, 1);
		ExpandingRectangle expRect = new ExpandingRectangle(startRect);
		expRect.setExpandHorizontally(true);
		expRect.setExpandVertically(true);
		expRect.setMaxExpansion(maze.numCols() - 1);
		return expRect.stream();
	}
}