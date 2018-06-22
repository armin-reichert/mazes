package de.amr.easy.maze.alg.ust;

import static de.amr.easy.graph.api.traversal.TraversalState.COMPLETED;
import static de.amr.easy.grid.api.GridPosition.CENTER;
import static java.lang.Math.max;

import de.amr.easy.grid.iterators.shapes.Circle;
import de.amr.easy.maze.alg.core.OrthogonalGrid;

/**
 * Wilson's algorithm where the vertices are selected from a collapsing circle.
 * 
 * @author Armin Reichert
 */
public class WilsonUSTCollapsingCircle extends WilsonUST {

	public WilsonUSTCollapsingCircle(OrthogonalGrid grid) {
		super(grid);
	}

	@Override
	public void run(int start) {
		start = grid.cell(CENTER);
		grid.set(start, COMPLETED);
		for (int radius = max(grid.numRows(), grid.numCols()) - 1; radius >= 0; radius--) {
			new Circle(grid, start, radius).forEach(this::loopErasedRandomWalk);
		}
	}
}