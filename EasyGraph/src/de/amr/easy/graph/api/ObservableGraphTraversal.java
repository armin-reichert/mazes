package de.amr.easy.graph.api;

import de.amr.easy.graph.api.event.GraphTraversalListener;

/**
 * Interface for observable graph traversals.
 * 
 * @author Armin Reichert
 */
public interface ObservableGraphTraversal extends GraphTraversal {

	void addObserver(GraphTraversalListener observer);

	void removeObserver(GraphTraversalListener observer);

	void edgeTouched(int u, int v);

	void vertexTouched(int u, TraversalState oldState, TraversalState newState);
}