package de.amr.easy.graph.api.event;

import de.amr.easy.graph.api.Edge;

/**
 * Information about a change of a vertex.
 * 
 * @author Armin Reichert
 *
 * @param <V>
 *          vertex type
 */
public class VertexEvent<V, E extends Edge> extends GraphEvent<V, E> {

	private int vertex;
	private V oldValue;
	private V newValue;

	public VertexEvent(ObservableGraph<V, E> graph, int vertex, V oldValue, V newValue) {
		super(graph);
		this.vertex = vertex;
		this.oldValue = oldValue;
		this.newValue = newValue;
	}

	public VertexEvent(ObservableGraph<V, E> graph, int vertex) {
		this(graph, vertex, null, null);
	}

	public int getVertex() {
		return vertex;
	}

	public V getOldValue() {
		return oldValue;
	}

	public V getNewValue() {
		return newValue;
	}
}