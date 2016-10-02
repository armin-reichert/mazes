package de.amr.easy.maze.algorithms.wilson;

import de.amr.easy.graph.api.TraversalState;
import de.amr.easy.graph.impl.DefaultEdge;
import de.amr.easy.grid.api.GridPosition;
import de.amr.easy.grid.api.ObservableDataGrid2D;
import de.amr.easy.grid.iterators.traversals.ExpandingCircle;

/**
 * Wilson's algorithm where the vertices are selected from an expanding circle.
 * 
 * @author Armin Reichert
 */
public class WilsonUSTExpandingCircle extends WilsonUST {

	public WilsonUSTExpandingCircle(ObservableDataGrid2D<Integer, DefaultEdge<Integer>, TraversalState> grid) {
		super(grid);
	}

	@Override
	protected Iterable<Integer> getCellSequence() {
		Integer center = grid.cell(GridPosition.CENTER);
		return new ExpandingCircle<>(grid, center, 1, Math.max(grid.numCols(), grid.numRows()));
	}

	@Override
	protected Integer modifyStartVertex(Integer start) {
		return grid.cell(GridPosition.CENTER);
	}
}
