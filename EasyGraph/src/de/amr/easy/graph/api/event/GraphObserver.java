package de.amr.easy.graph.api.event;

import de.amr.easy.graph.api.Edge;

/**
 * Observer interface for graph operations.
 * 
 * @author Armin Reichert
 * 
 * @param <V>
 *          vertex type
 */
public interface GraphObserver<V, E extends Edge> {

	/**
	 * Called when a vertex has changed.
	 * 
	 * @param event
	 *          information about the vertex change
	 */
	void vertexChanged(VertexEvent<V, E> event);

	/**
	 * Called when an edge has changed or was touched.
	 * 
	 * @param event
	 *          information about the edge
	 */
	void edgeChanged(EdgeEvent<V, E> event);

	/**
	 * Called when an edge has been added.
	 * 
	 * @param event
	 *          information about the new edge
	 */
	void edgeAdded(EdgeEvent<V, E> event);

	/**
	 * Called when an edge has been removed.
	 * 
	 * @param event
	 *          information about the removed edge
	 */
	void edgeRemoved(EdgeEvent<V, E> event);

	/**
	 * Called when the graph as a whole has changed.
	 * 
	 * @param graph
	 *          the new or changed graph
	 */
	void graphChanged(ObservableGraph<V, E> graph);
}