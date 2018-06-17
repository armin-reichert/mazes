package de.amr.easy.graph.api.event;

/**
 * Observer interface for graph operations.
 * 
 * @author Armin Reichert
 */
public interface GraphObserver {

	/**
	 * Called when a vertex has changed.
	 * 
	 * @param event
	 *          information about the vertex change
	 */
	void vertexChanged(VertexEvent event);

	/**
	 * Called when an edge has changed or was touched.
	 * 
	 * @param event
	 *          information about the edge
	 */
	void edgeChanged(EdgeEvent event);

	/**
	 * Called when an edge has been added.
	 * 
	 * @param event
	 *          information about the new edge
	 */
	void edgeAdded(EdgeEvent event);

	/**
	 * Called when an edge has been removed.
	 * 
	 * @param event
	 *          information about the removed edge
	 */
	void edgeRemoved(EdgeEvent event);

	/**
	 * Called when the graph as a whole has changed.
	 * 
	 * @param graph
	 *          the new or changed graph
	 */
	void graphChanged(ObservableGraph<?> graph);
}