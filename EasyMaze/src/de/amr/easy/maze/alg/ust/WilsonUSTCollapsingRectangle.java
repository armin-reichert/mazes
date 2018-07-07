package de.amr.easy.maze.alg.ust;

import static de.amr.easy.graph.api.traversal.TraversalState.COMPLETED;
import static de.amr.easy.grid.api.GridPosition.CENTER;

import de.amr.easy.grid.impl.iterators.shapes.Rectangle;
import de.amr.easy.maze.alg.core.OrthogonalGrid;

/**
 * Wilson's algorithm where the vertices are selected from a collapsing rectangle.
 * 
 * @author Armin Reichert
 */
public class WilsonUSTCollapsingRectangle extends WilsonUST {

	public WilsonUSTCollapsingRectangle(int numCols, int numRows) {
		super(numCols, numRows);
	}

	@Override
	public OrthogonalGrid createMaze(int x, int y) {
		int start = grid.cell(CENTER);
		grid.set(start, COMPLETED);
		int col = 0, row = 0;
		int width = grid.numCols(), height = grid.numRows();
		while (width > 0 && height > 0) {
			new Rectangle(grid, grid.cell(col, row), width, height).forEach(this::loopErasedRandomWalk);
			width -= 2;
			height -= 2;
			col += 1;
			row += 1;
		}
		return grid;
	}
}