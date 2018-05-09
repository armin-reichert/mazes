package de.amr.easy.maze.alg.ust;

import static de.amr.easy.grid.api.GridPosition.CENTER;
import static de.amr.easy.grid.iterators.IteratorFactory.parallel;
import static de.amr.easy.grid.iterators.IteratorFactory.sequence;
import static java.lang.Math.max;

import java.util.Iterator;
import java.util.stream.IntStream;
import java.util.stream.StreamSupport;

import de.amr.easy.graph.api.TraversalState;
import de.amr.easy.grid.api.Grid2D;
import de.amr.easy.grid.iterators.traversals.ExpandingCircle;

/**
 * Wilson's algorithm where grid cells are selected from five expanding circles.
 * 
 * @author Armin Reichert
 */
public class WilsonUSTExpandingCircles extends WilsonUST {

	public WilsonUSTExpandingCircles(Grid2D<TraversalState, Integer> grid) {
		super(grid);
	}

	@Override
	protected int customizedStartCell(int start) {
		return grid.cell(CENTER);
	}

	@Override
	protected IntStream cellStream() {
		Iterable<Integer> it = new Iterable<Integer>() {

			@Override
			public Iterator<Integer> iterator() {
				int w = grid.numCols(), h = grid.numRows(), r = max(w / 2, h / 2);
				/*@formatter:off*/
				return sequence(
					// expand 4 circles in parallel to certain size	
					parallel(
						expandingCircle(w / 4, h / 4, 1, r / 4),
						expandingCircle(3 * w / 4, h / 4, 1, r / 4),
						expandingCircle(w / 4, 3 * h / 4, 1, r / 4), 
						expandingCircle(3 * w / 4, 3 * h / 4, 1, r / 4)
					),
					// expand 5th circle to half its size
					expandingCircle(w / 2, h / 2, 1, r / 2),
					// expand first 4 circles to final size
					parallel(
						expandingCircle(w / 4, h / 4, r / 4, r / 2),
						expandingCircle(3 * w / 4, h / 4, r / 4, r / 2), 
						expandingCircle(w / 4, 3 * h / 4, r / 4, r / 2),
						expandingCircle(3 * w / 4, 3 * h / 4, r / 4, r / 2)
					),
					// expand 5th circle to final size
					expandingCircle(w / 2, h / 2, r / 2, 2 * r)
				);
				/*@formatter:on*/
			}
		};
		return StreamSupport.stream(it.spliterator(), false).mapToInt(Integer::intValue); // TODO
	}

	private Iterator<Integer> expandingCircle(int centerX, int centerY, int rmin, int rmax) {
		return new ExpandingCircle(grid, grid.cell(centerX, centerY), rmin, rmax).iterator();
	}
}