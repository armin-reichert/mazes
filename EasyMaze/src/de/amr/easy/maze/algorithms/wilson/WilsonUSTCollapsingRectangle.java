package de.amr.easy.maze.algorithms.wilson;

import static de.amr.easy.grid.api.GridPosition.CENTER;

import de.amr.easy.graph.api.TraversalState;
import de.amr.easy.grid.api.Grid2D;
import de.amr.easy.grid.iterators.shapes.Rectangle;

/**
 * Wilson's algorithm where the vertices are selected from a collapsing rectangle.
 * 
 * @author Armin Reichert
 */
public class WilsonUSTCollapsingRectangle extends WilsonUST {

	private int width, height;

	public WilsonUSTCollapsingRectangle(Grid2D<TraversalState> grid) {
		super(grid);
		width = grid.numCols();
		height = grid.numRows();
	}

	@Override
	public void accept(Integer start) {
		start = grid.cell(CENTER);
		addCellToTree(start);
		int col = 0, row = 0;
		while (width > 0 && height > 0) {
			new Rectangle(grid, grid.cell(col, row), width, height).forEach(walkStart -> {
				if (isOutsideTree(walkStart)) {
					loopErasedRandomWalk(walkStart);
				}
			});
			width -= 2;
			height -= 2;
			++col;
			++row;
		}
	}
}