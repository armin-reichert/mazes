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

	public WilsonUSTCollapsingCircle(int numCols, int numRows) {
		super(numCols, numRows);
	}

	@Override
	public OrthogonalGrid createMaze(int x, int y) {
		int center = maze.cell(CENTER);
		maze.set(center, COMPLETED);
		for (int r = max(maze.numRows(), maze.numCols()) - 1; r > 0; r--) {
			new Circle(maze, center, r).forEach(walkStart -> loopErasedRandomWalk(walkStart));
		}
		return maze;
	}
}