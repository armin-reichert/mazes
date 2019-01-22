package de.amr.easy.maze.alg.ust;

import static de.amr.easy.graph.grid.api.GridPosition.CENTER;
import static de.amr.easy.graph.grid.iterators.IteratorFactory.parallel;
import static de.amr.easy.graph.grid.iterators.IteratorFactory.sequence;
import static java.lang.Math.max;

import java.util.Iterator;
import java.util.stream.IntStream;

import de.amr.easy.graph.grid.impl.OrthogonalGrid;
import de.amr.easy.graph.grid.traversals.ExpandingCircle;
import de.amr.easy.util.StreamUtils;

/**
 * Wilson's algorithm where grid cells are selected from five expanding circles.
 * 
 * @author Armin Reichert
 */
public class WilsonUSTExpandingCircles extends WilsonUST {

	public WilsonUSTExpandingCircles(int numCols, int numRows) {
		super(numCols, numRows);
	}

	@Override
	public OrthogonalGrid createMaze(int x, int y) {
		return runWilsonAlgorithm(grid.cell(CENTER));
	}

	@Override
	protected IntStream randomWalkStartCells() {
		Iterable<Integer> it = new Iterable<Integer>() {

			@Override
			public Iterator<Integer> iterator() {
				int w = grid.numCols(), h = grid.numRows(), r = max(w / 2, h / 2);
				/*@formatter:off*/
				return sequence(
					// expand 4 circles in parallel to certain size	
					parallel(
						expandingCircle(grid, w / 4, h / 4, 1, r / 4),
						expandingCircle(grid, 3 * w / 4, h / 4, 1, r / 4),
						expandingCircle(grid, w / 4, 3 * h / 4, 1, r / 4), 
						expandingCircle(grid, 3 * w / 4, 3 * h / 4, 1, r / 4)
					),
					// expand 5th circle to half its size
					expandingCircle(grid, w / 2, h / 2, 1, r / 2),
					// expand first 4 circles to final size
					parallel(
						expandingCircle(grid, w / 4, h / 4, r / 4, r / 2),
						expandingCircle(grid, 3 * w / 4, h / 4, r / 4, r / 2), 
						expandingCircle(grid, w / 4, 3 * h / 4, r / 4, r / 2),
						expandingCircle(grid, 3 * w / 4, 3 * h / 4, r / 4, r / 2)
					),
					// expand 5th circle to final size
					expandingCircle(grid, w / 2, h / 2, r / 2, 2 * r)
				);
				/*@formatter:on*/
			}
		};
		return StreamUtils.toIntStream(it);
	}

	private Iterator<Integer> expandingCircle(OrthogonalGrid grid, int centerX, int centerY, int rmin,
			int rmax) {
		return new ExpandingCircle(grid, grid.cell(centerX, centerY), rmin, rmax).iterator();
	}
}