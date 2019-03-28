package de.amr.maze.alg.core;

import de.amr.graph.core.api.TraversalState;
import de.amr.graph.core.api.UndirectedEdge;
import de.amr.graph.grid.impl.ObservableGridGraph;
import de.amr.graph.grid.impl.Top4;

/**
 * Type of 2D grid used by maze generator implementations.
 * 
 * @author Armin Reichert
 */
public class OrthogonalGrid extends ObservableGridGraph<TraversalState, Integer> {

	public static OrthogonalGrid emptyGrid(int numCols, int numRows, TraversalState defaultState) {
		OrthogonalGrid grid = new OrthogonalGrid(numCols, numRows, defaultState);
		return grid;
	}

	public static OrthogonalGrid fullGrid(int numCols, int numRows, TraversalState defaultState) {
		OrthogonalGrid grid = new OrthogonalGrid(numCols, numRows, defaultState);
		grid.fill();
		return grid;
	}

	private OrthogonalGrid(int numCols, int numRows, TraversalState defaultState) {
		super(numCols, numRows, Top4.get(), v -> defaultState, (u, v) -> 1, UndirectedEdge::new);
	}

	public boolean isUnvisited(int v) {
		return get(v) == TraversalState.UNVISITED;
	}

	public boolean isVisited(int v) {
		return get(v) == TraversalState.VISITED;
	}

	public boolean isCompleted(int v) {
		return get(v) == TraversalState.COMPLETED;
	}
}