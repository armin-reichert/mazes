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
public class OrthogonalGrid extends ObservableGridGraph<TraversalState, Void> {

	public OrthogonalGrid(int numCols, int numRows) {
		super(numCols, numRows, Top4.get(), SimpleEdge::new);
		setDefaultVertex(TraversalState.UNVISITED);
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