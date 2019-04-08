package de.amr.maze.alg.core;

import de.amr.graph.core.api.TraversalState;
import de.amr.graph.core.api.UndirectedEdge;
import de.amr.graph.grid.api.GridGraph2D;
import de.amr.graph.grid.impl.GridGraph;
import de.amr.graph.grid.impl.Top4;

/**
 * Factory for creating non-observable grid graphs.
 * 
 * @author Armin Reichert
 */
public class UnobservableGridFactory {

	private static final UnobservableGridFactory it = new UnobservableGridFactory();

	public static final UnobservableGridFactory get() {
		return it;
	}

	public GridGraph2D<TraversalState, Integer> emptyGrid(int numCols, int numRows,
			TraversalState defaultState) {
		return new GridGraph<>(numCols, numRows, Top4.get(), cell -> defaultState, (u, v) -> 1,
				UndirectedEdge::new);
	}

	public GridGraph2D<TraversalState, Integer> fullGrid(int numCols, int numRows,
			TraversalState defaultState) {
		GridGraph<TraversalState, Integer> grid = new GridGraph<>(numCols, numRows, Top4.get(),
				cell -> defaultState, (u, v) -> 1, UndirectedEdge::new);
		grid.fill();
		return grid;
	}
}