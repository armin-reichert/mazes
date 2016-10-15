package de.amr.easy.graph.event;

import de.amr.easy.graph.api.Edge;
import de.amr.easy.graph.api.ObservableGraph;

/**
 * Listener interface for graph operations.
 * 
 * @param <V>
 *          vertex data type
 * 
 * @param <E>
 *          edge data type
 * 
 * @author Armin Reichert
 */
public interface GraphListener<V, E extends Edge<V>> {

	public void vertexChanged(VertexChangeEvent<V, E> event);

	public void edgeChanged(EdgeChangeEvent<V, E> event);

	public void edgeAdded(EdgeAddedEvent<V, E> event);

	public void edgeRemoved(EdgeRemovedEvent<V, E> event);

	public void graphChanged(ObservableGraph<V, E> graph);
}