package de.amr.easy.graph.api;

import de.amr.easy.graph.event.GraphListener;

/**
 * A graph whose vertex and edge state can be observed by listeners.
 * 
 * @param <V>
 *          the graph vertex type
 * @param <E>
 *          the edge type
 */
public interface ObservableGraph<V, E> extends Graph<V, E> {

	public void addGraphListener(GraphListener<V, E> listener);

	public void removeGraphListener(GraphListener<V, E> listener);

	public void setEventsEnabled(boolean enabled);

	public void fireVertexChange(V v, Object oldValue, Object newValue);

	public void fireEdgeChange(E edge, Object oldValue, Object newValue);

	public void fireGraphChange(ObservableGraph<V, E> graph);
}
