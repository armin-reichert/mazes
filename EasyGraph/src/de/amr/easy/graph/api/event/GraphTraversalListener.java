package de.amr.easy.graph.api.event;

import de.amr.easy.graph.api.TraversalState;

/**
 * A listener for graph traversals like BFS.
 * 
 * @author Armin Reichert
 */
public interface GraphTraversalListener {

	public void vertexTouched(int vertex, TraversalState oldState, TraversalState newState);

	public void edgeTouched(int source, int target);
}
