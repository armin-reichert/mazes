package de.amr.maze.alg.ust;

import static de.amr.graph.grid.api.GridPosition.TOP_LEFT;

import java.util.stream.IntStream;

import de.amr.graph.grid.impl.OrthogonalGrid;
import de.amr.graph.grid.shapes.Rectangle;
import de.amr.graph.grid.traversals.ExpandingRectangle;

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
		return runWilsonAlgorithm(grid.cell(TOP_LEFT));
	}

	@Override
	protected IntStream randomWalkStartCells() {
		Rectangle startRect = new Rectangle(grid, grid.cell(TOP_LEFT), 1, 1);
		ExpandingRectangle expRect = new ExpandingRectangle(startRect);
		expRect.setExpandHorizontally(true);
		expRect.setExpandVertically(true);
		expRect.setMaxExpansion(grid.numCols() - 1);
		return expRect.stream();
	}
}