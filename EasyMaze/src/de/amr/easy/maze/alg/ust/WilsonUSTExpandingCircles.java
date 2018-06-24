package de.amr.easy.maze.alg.ust;

import static de.amr.easy.grid.api.GridPosition.CENTER;
import static de.amr.easy.grid.iterators.IteratorFactory.parallel;
import static de.amr.easy.grid.iterators.IteratorFactory.sequence;
import static java.lang.Math.max;

import java.util.Iterator;
import java.util.stream.IntStream;

import de.amr.easy.grid.iterators.traversals.ExpandingCircle;
import de.amr.easy.maze.alg.core.OrthogonalGrid;
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
		return runWilsonAlgorithm(maze, maze.cell(CENTER));
	}

	@Override
	protected IntStream randomWalkStartCells(OrthogonalGrid maze) {
		Iterable<Integer> it = new Iterable<Integer>() {

			@Override
			public Iterator<Integer> iterator() {
				int w = maze.numCols(), h = maze.numRows(), r = max(w / 2, h / 2);
				/*@formatter:off*/
				return sequence(
					// expand 4 circles in parallel to certain size	
					parallel(
						expandingCircle(maze, w / 4, h / 4, 1, r / 4),
						expandingCircle(maze, 3 * w / 4, h / 4, 1, r / 4),
						expandingCircle(maze, w / 4, 3 * h / 4, 1, r / 4), 
						expandingCircle(maze, 3 * w / 4, 3 * h / 4, 1, r / 4)
					),
					// expand 5th circle to half its size
					expandingCircle(maze, w / 2, h / 2, 1, r / 2),
					// expand first 4 circles to final size
					parallel(
						expandingCircle(maze, w / 4, h / 4, r / 4, r / 2),
						expandingCircle(maze, 3 * w / 4, h / 4, r / 4, r / 2), 
						expandingCircle(maze, w / 4, 3 * h / 4, r / 4, r / 2),
						expandingCircle(maze, 3 * w / 4, 3 * h / 4, r / 4, r / 2)
					),
					// expand 5th circle to final size
					expandingCircle(maze, w / 2, h / 2, r / 2, 2 * r)
				);
				/*@formatter:on*/
			}
		};
		return StreamUtils.toIntStream(it);
	}

	private Iterator<Integer> expandingCircle(OrthogonalGrid maze, int centerX, int centerY, int rmin, int rmax) {
		return new ExpandingCircle(maze, maze.cell(centerX, centerY), rmin, rmax).iterator();
	}
}