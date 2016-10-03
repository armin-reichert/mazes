package de.amr.easy.maze.algorithms.wilson;

import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import de.amr.easy.graph.api.TraversalState;
import de.amr.easy.graph.impl.DefaultEdge;
import de.amr.easy.grid.api.ObservableDataGrid2D;
import de.amr.easy.grid.iterators.shapes.Rectangle;
import de.amr.easy.grid.iterators.traversals.ExpandingRectangle;

/**
 * Wilson's algorithm where the vertices are selected from several circles in turn.
 * 
 * @author Armin Reichert
 */
public class WilsonUSTExpandingRectangle extends WilsonUST {

	public WilsonUSTExpandingRectangle(ObservableDataGrid2D<Integer, DefaultEdge<Integer>, TraversalState> grid) {
		super(grid);
	}

	@Override
	protected Stream<Integer> getCellSequence() {
		Rectangle<Integer> startRectangle = new Rectangle<>(grid, grid.cell(0, 0), 1, 1);
		ExpandingRectangle<Integer> expRect = new ExpandingRectangle<>(startRectangle);
		expRect.setExpandHorizontally(true);
		expRect.setExpandVertically(true);
		expRect.setMaxExpansion(grid.numCols() - 1);
		return StreamSupport.stream(expRect.spliterator(), false);
	}

	@Override
	protected Integer getStartCell(Integer start) {
		return grid.cell(0, 0);
	}
}
