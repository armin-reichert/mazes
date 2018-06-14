package de.amr.easy.graph.api.event;

import de.amr.easy.graph.api.ObservableGraph;

/**
 * Listener interface for graph operations.
 * 
 * @param <E>
 *          edge data type
 * 
 * @author Armin Reichert
 */
public interface GraphObserver {

	public void vertexChanged(VertexChangeEvent event);

	public void edgeChanged(EdgeChangeEvent event);

	public void edgeAdded(EdgeAddedEvent event);

	public void edgeRemoved(EdgeRemovedEvent event);

	public void graphChanged(ObservableGraph<?> graph);
}