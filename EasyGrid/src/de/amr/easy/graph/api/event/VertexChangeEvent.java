package de.amr.easy.graph.api.event;

import de.amr.easy.graph.api.Edge;
import de.amr.easy.graph.api.ObservableGraph;

/**
 * Event signaling a change of a vertex.
 * 
 * @author Armin Reichert
 *
 * @param <V>
 *          vertex type
 * @param <E>
 *          edge type
 */
public class VertexChangeEvent<V, E extends Edge<V>> {

	private V vertex;
	private Object oldValue;
	private Object newValue;

	public VertexChangeEvent(ObservableGraph<V, E> graph, V vertex, Object oldValue, Object newValue) {
		this.vertex = vertex;
		this.oldValue = oldValue;
		this.newValue = newValue;
	}

	public V getVertex() {
		return vertex;
	}

	public Object getOldValue() {
		return oldValue;
	}

	public Object getNewValue() {
		return newValue;
	}

}