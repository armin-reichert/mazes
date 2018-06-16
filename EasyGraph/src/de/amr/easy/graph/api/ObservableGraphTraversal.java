package de.amr.easy.graph.api;

import de.amr.easy.graph.api.event.GraphTraversalObserver;

/**
 * Interface for observable graph traversals.
 * 
 * @author Armin Reichert
 */
public interface ObservableGraphTraversal extends GraphTraversal {

	void addObserver(GraphTraversalObserver observer);

	void removeObserver(GraphTraversalObserver observer);

	void edgeTouched(int u, int v);

	void vertexVisited(int u, TraversalState oldState, TraversalState newState);
}