package de.amr.maze.alg.core;

import de.amr.graph.core.api.TraversalState;
import de.amr.graph.core.api.UndirectedEdge;
import de.amr.graph.grid.api.ObservableGridGraph2D;
import de.amr.graph.grid.impl.ObservableGridGraph;
import de.amr.graph.grid.impl.Top4;

/**
 * Factory for creating observable grid graphs.
 * 
 * @author Armin Reichert
 */
public class ObservableGridFactory {

	private static final ObservableGridFactory it = new ObservableGridFactory();

	public static ObservableGridFactory get() {
		return it;
	}

	public ObservableGridGraph2D<TraversalState, Integer> emptyGrid(int numCols, int numRows,
			TraversalState defaultState) {
		return new ObservableGridGraph<>(numCols, numRows, Top4.get(), cell -> defaultState, (u, v) -> 1,
				UndirectedEdge::new);
	}

	public ObservableGridGraph2D<TraversalState, Integer> fullGrid(int numCols, int numRows,
			TraversalState defaultState) {
		ObservableGridGraph<TraversalState, Integer> grid = new ObservableGridGraph<>(numCols, numRows,
				Top4.get(), cell -> defaultState, (u, v) -> 1, UndirectedEdge::new);
		grid.fill();
		return grid;
	}
}