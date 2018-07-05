package de.amr.easy.maze.alg.core;

import de.amr.easy.graph.api.SimpleEdge;
import de.amr.easy.graph.api.traversal.TraversalState;
import de.amr.easy.grid.impl.ObservableGridGraph;
import de.amr.easy.grid.impl.Top4;

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
		super(numCols, numRows, Top4.get(), defaultState, (u, v) -> 1, SimpleEdge::new);
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