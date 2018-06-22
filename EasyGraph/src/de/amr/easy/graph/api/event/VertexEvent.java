package de.amr.easy.graph.api.event;

/**
 * Information about a change of a vertex.
 * 
 * @author Armin Reichert
 *
 * @param <V>
 *          vertex label type
 * @param <E>
 *          edge label type
 */
public class VertexEvent<V, E> extends GraphEvent<V, E> {

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