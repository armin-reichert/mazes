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

	public void vertexChanged(V vertex, Object oldValue, Object newValue);

	public void edgeChanged(E edge, Object oldValue, Object newValue);

	public void edgeAdded(E edge);

	public void edgeRemoved(E edge);

	public void graphChanged(ObservableGraph<V, E> graph);
}
