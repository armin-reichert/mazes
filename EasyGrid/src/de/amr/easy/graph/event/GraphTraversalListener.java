package de.amr.easy.graph.event;

import de.amr.easy.graph.api.TraversalState;

/**
 * A listener for graph traversals like BFS.
 * 
 * @author Armin Reichert
 *
 * @param <V>
 *          vertex type
 */
public interface GraphTraversalListener<V> {

	public void vertexTouched(V vertex, TraversalState oldState, TraversalState newState);

	public void edgeTouched(V source, V target);
}
