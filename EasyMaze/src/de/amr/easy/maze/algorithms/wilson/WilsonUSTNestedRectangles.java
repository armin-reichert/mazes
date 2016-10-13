package de.amr.easy.maze.algorithms.wilson;

import static de.amr.easy.grid.api.GridPosition.TOP_LEFT;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import de.amr.easy.graph.api.TraversalState;
import de.amr.easy.grid.api.ObservableDataGrid2D;
import de.amr.easy.grid.iterators.IteratorFactory;
import de.amr.easy.grid.iterators.shapes.Rectangle;
import de.amr.easy.grid.iterators.traversals.ExpandingRectangle;

/**
 * Wilson's algorithm where the vertices are selected from a sequence of nested rectangles.
 * 
 * @author Armin Reichert
 */
public class WilsonUSTNestedRectangles extends WilsonUST {

	public WilsonUSTNestedRectangles(ObservableDataGrid2D<Integer, TraversalState> grid) {
		super(grid);
	}

	@Override
	protected Integer getStartCell(Integer start) {
		return grid.cell(TOP_LEFT);
	}

	@Override
	protected Stream<Integer> cellStream() {
		Iterable<Integer> it = new Iterable<Integer>() {

			@Override
			public Iterator<Integer> iterator() {
				Rectangle<Integer> firstCell = new Rectangle<>(grid, grid.cell(TOP_LEFT), 1, 1);
				List<Iterator<Integer>> expRects = new ArrayList<>();
				int rate = grid.numCols();
				while (rate > 1) {
					expRects.add(expandingRectangle(firstCell, rate).iterator());
					rate /= 2;
				}
				@SuppressWarnings("unchecked")
				Iterator<Integer>[] expRectsArray = expRects.toArray(new Iterator[expRects.size()]);

				Rectangle<Integer> firstColumn = new Rectangle<>(grid, grid.cell(TOP_LEFT), 1, grid.numRows());
				ExpandingRectangle<Integer> sweep = new ExpandingRectangle<>(firstColumn);
				sweep.setExpandHorizontally(true);
				sweep.setExpandVertically(false);
				sweep.setExpansionRate(1);
				sweep.setMaxExpansion(grid.numCols());

				return IteratorFactory.sequence(IteratorFactory.sequence(expRectsArray), sweep.iterator());
			}
		};
		return StreamSupport.stream(it.spliterator(), false);
	}

	private ExpandingRectangle<Integer> expandingRectangle(Rectangle<Integer> startRectangle, int rate) {
		ExpandingRectangle<Integer> r = new ExpandingRectangle<>(startRectangle);
		r.setExpandHorizontally(true);
		r.setExpandVertically(true);
		r.setExpansionRate(rate);
		r.setMaxExpansion(grid.numCols() - startRectangle.getWidth());
		return r;
	}
}