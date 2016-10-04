package de.amr.easy.maze.algorithms.wilson;

import java.util.stream.Stream;
import java.util.stream.StreamSupport;

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
	protected Stream<Integer> getCellSequence() {
		Integer center = grid.cell(GridPosition.CENTER);
		Iterable<Integer> it = new ExpandingCircle<>(grid, center, 1, Math.max(grid.numCols(), grid.numRows()));
		return StreamSupport.stream(it.spliterator(), false);
	}

	@Override
	protected Integer getStartCell(Integer start) {
		return grid.cell(GridPosition.CENTER);
	}
}
