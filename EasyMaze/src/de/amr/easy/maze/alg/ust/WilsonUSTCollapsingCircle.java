package de.amr.easy.maze.alg.ust;

import static de.amr.easy.grid.api.GridPosition.CENTER;
import static java.lang.Math.max;

import de.amr.easy.graph.api.SimpleEdge;
import de.amr.easy.graph.api.TraversalState;
import de.amr.easy.grid.api.Grid2D;
import de.amr.easy.grid.iterators.shapes.Circle;

/**
 * Wilson's algorithm where the vertices are selected from a collapsing circle.
 * 
 * @author Armin Reichert
 */
public class WilsonUSTCollapsingCircle extends WilsonUST {

	public WilsonUSTCollapsingCircle(Grid2D<TraversalState, SimpleEdge> grid) {
		super(grid);
	}

	@Override
	public void run(int start) {
		start = grid.cell(CENTER);
		addToTree(start);
		for (int radius = max(grid.numRows(), grid.numCols()) - 1; radius >= 0; radius--) {
			new Circle(grid, start, radius).forEach(this::loopErasedRandomWalk);
		}
	}
}