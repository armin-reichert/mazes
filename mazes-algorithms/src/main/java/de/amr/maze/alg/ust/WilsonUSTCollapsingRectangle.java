package de.amr.maze.alg.ust;

import static de.amr.graph.core.api.TraversalState.COMPLETED;
import static de.amr.graph.grid.api.GridPosition.CENTER;

import de.amr.graph.core.api.TraversalState;
import de.amr.graph.grid.api.GridGraph2D;
import de.amr.graph.grid.shapes.Rectangle;
import de.amr.maze.alg.core.MazeGridFactory;

/**
 * Wilson's algorithm where the vertices are selected from a collapsing rectangle.
 * 
 * @author Armin Reichert
 */
public class WilsonUSTCollapsingRectangle extends WilsonUST {

	public WilsonUSTCollapsingRectangle(MazeGridFactory factory, int numCols, int numRows) {
		super(factory, numCols, numRows);
	}

	@Override
	public GridGraph2D<TraversalState, Integer> createMaze(int x, int y) {
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