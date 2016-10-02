package de.amr.easy.graph.event;

import de.amr.easy.graph.api.ObservableGraph;

public class VertexChangeEvent<V, E> {

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
