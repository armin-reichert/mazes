package de.amr.easy.maze.alg.ust;

import static de.amr.easy.grid.api.GridPosition.CENTER;

import de.amr.easy.graph.api.SimpleEdge;
import de.amr.easy.graph.api.TraversalState;
import de.amr.easy.grid.api.Grid2D;
import de.amr.easy.grid.iterators.shapes.Rectangle;

/**
 * Wilson's algorithm where the vertices are selected from a collapsing rectangle.
 * 
 * @author Armin Reichert
 */
public class WilsonUSTCollapsingRectangle extends WilsonUST {

	public WilsonUSTCollapsingRectangle(Grid2D<TraversalState, SimpleEdge> grid) {
		super(grid);
	}

	@Override
	public void run(int start) {
		start = grid.cell(CENTER);
		addToTree(start);
		int col = 0, row = 0;
		int width = grid.numCols(), height = grid.numRows();
		while (width > 0 && height > 0) {
			new Rectangle(grid, grid.cell(col, row), width, height).forEach(this::loopErasedRandomWalk);
			width -= 2;
			height -= 2;
			col += 1;
			row += 1;
		}
	}
}