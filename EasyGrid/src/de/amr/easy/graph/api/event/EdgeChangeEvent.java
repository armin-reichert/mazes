package de.amr.easy.graph.api.event;

import de.amr.easy.graph.api.Edge;
import de.amr.easy.graph.api.ObservableGraph;

/**
 * Event for edge changes.
 * 
 * @author Armin Reichert
 *
 * @param <V>
 *          the vertex type
 * @param <E>
 *          the edge type
 */
public class EdgeChangeEvent<V, E extends Edge<V>> {

	private final ObservableGraph<V, E> graph;
	private final E edge;
	private final Object oldValue;
	private final Object newValue;

	public EdgeChangeEvent(ObservableGraph<V, E> graph, E edge, Object oldValue, Object newValue) {
		this.graph = graph;
		this.edge = edge;
		this.oldValue = oldValue;
		this.newValue = newValue;
	}

	public ObservableGraph<V, E> getGraph() {
		return graph;
	}

	public E getEdge() {
		return edge;
	}

	public Object getOldValue() {
		return oldValue;
	}

	public Object getNewValue() {
		return newValue;
	}
}
