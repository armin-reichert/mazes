package de.amr.easy.maze.algorithms.wilson;

import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import de.amr.easy.graph.api.TraversalState;
import de.amr.easy.graph.impl.DefaultEdge;
import de.amr.easy.grid.api.ObservableDataGrid2D;
import de.amr.easy.grid.iterators.traversals.RightToLeftSweep;

/**
 * Wilson's algorithm where the vertices are selected column-wise left-to-right.
 * 
 * @author Armin Reichert
 */
public class WilsonUSTRightToLeftSweep extends WilsonUST {

	public WilsonUSTRightToLeftSweep(
			ObservableDataGrid2D<Integer, DefaultEdge<Integer>, TraversalState> grid) {
		super(grid);
	}

	@Override
	protected Stream<Integer> getCellSequence() {
		return StreamSupport.stream(new RightToLeftSweep<>(grid).spliterator(), false);
	}

	@Override
	protected Integer getStartCell(Integer start) {
		return grid.cell(grid.numCols() - 1, grid.numRows() - 1);
	}
}
