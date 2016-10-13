package de.amr.easy.maze.algorithms.wilson;

import java.util.stream.Stream;

import de.amr.easy.graph.api.TraversalState;
import de.amr.easy.graph.impl.DefaultEdge;
import de.amr.easy.grid.api.ObservableDataGrid2D;
import de.amr.easy.grid.iterators.traversals.RecursiveCrosses;

/**
 * Wilson's algorithm where the vertices are selected from recursive crosses.
 * 
 * @author Armin Reichert
 */
public class WilsonUSTRecursiveCrosses extends WilsonUST {

	public WilsonUSTRecursiveCrosses(ObservableDataGrid2D<Integer, DefaultEdge<Integer>, TraversalState> grid) {
		super(grid);
	}

	@Override
	protected Stream<Integer> cellStream() {
		return new RecursiveCrosses<>(grid).stream();
	}
}