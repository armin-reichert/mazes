package de.amr.easy.graph.api.event;

import de.amr.easy.graph.api.Edge;
import de.amr.easy.graph.api.ObservableGraph;

/**
 * Listener interface for graph operations.
 * 
 * @param <E>
 *          edge data type
 * 
 * @author Armin Reichert
 */
public interface GraphObserver<E extends Edge> {

	public void vertexChanged(VertexChangeEvent event);

	public void edgeChanged(EdgeChangeEvent<E> event);

	public void edgeAdded(EdgeAddedEvent<E> event);

	public void edgeRemoved(EdgeRemovedEvent<E> event);

	public void graphChanged(ObservableGraph<E> graph);
}