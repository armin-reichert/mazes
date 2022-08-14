package de.amr.maze.alg.ust;

import static de.amr.graph.grid.api.GridPosition.CENTER;
import static de.amr.graph.grid.iterators.IteratorFactory.parallel;
import static de.amr.graph.grid.iterators.IteratorFactory.sequence;
import static java.lang.Math.max;

import java.util.Iterator;
import java.util.stream.IntStream;

import de.amr.datastruct.StreamUtils;
import de.amr.graph.core.api.TraversalState;
import de.amr.graph.grid.api.GridGraph2D;
import de.amr.graph.grid.traversals.ExpandingCircle;

/**
 * Wilson's algorithm where grid cells are selected from five expanding circles.
 * 
 * @author Armin Reichert
 */
public class WilsonUSTExpandingCircles extends WilsonUST {

	public WilsonUSTExpandingCircles(GridGraph2D<TraversalState, Integer> grid) {
		super(grid);
	}

	@Override
	public void createMaze(int x, int y) {
		runWilsonAlgorithm(grid.cell(CENTER));
	}

	@Override
	protected IntStream randomWalkStartCells() {
		Iterable<Integer> it = () -> {
			int w = grid.numCols();
			int w2 = w / 2;
			int w4 = w / 4;
			int h = grid.numRows();
			int h2 = h / 2;
			int h4 = h / 4;
			int r = max(w2, h2);
			int r2 = r / 2;
			int r4 = r / 4;
			/*@formatter:off*/
			return sequence(
				// expand 4 circles in parallel to certain size	
				parallel(
					expandingCircle(grid, w4, h4, 1, r4),
					expandingCircle(grid, 3 * w4, h4, 1, r4),
					expandingCircle(grid, w4, 3 * h4, 1, r4), 
					expandingCircle(grid, 3 * w4, 3 * h4, 1, r4)
				),
				// expand 5th circle to half its size
				expandingCircle(grid, w2, h2, 1, r2),
				// expand first 4 circles to final size
				parallel(
					expandingCircle(grid, w4, h4, r4, r2),
					expandingCircle(grid, 3 * w4, h4, r4, r2), 
					expandingCircle(grid, w4, 3 * h4, r4, r2),
					expandingCircle(grid, 3 * w4, 3 * h4, r4, r2)
				),
				// expand 5th circle to final size
				expandingCircle(grid, w2, h2, r2, 2 * r)
			);
			/*@formatter:on*/
		};
		return StreamUtils.toIntStream(it);
	}

	private Iterator<Integer> expandingCircle(GridGraph2D<TraversalState, Integer> grid, int centerX, int centerY,
			int rmin, int rmax) {
		return new ExpandingCircle(grid, grid.cell(centerX, centerY), rmin, rmax).iterator();
	}
}