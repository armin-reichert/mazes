package de.amr.easy.graph.event;

import de.amr.easy.graph.api.Edge;
import de.amr.easy.graph.api.ObservableGraph;

/**
 * Event for edge state changes.
 * 
 * @author Armin Reichert
 *
 * @param <V>
 *          the vertex type
 * @param <E>
 *          the edge type
 */
public class EdgeChangeEvent<V, E extends Edge<V>> extends EdgeEvent<V, E> {

	private Object oldValue;
	private Object newValue;

	public EdgeChangeEvent(ObservableGraph<V, E> graph, E edge, Object oldValue, Object newValue) {
		super(graph, edge);
		this.oldValue = oldValue;
		this.newValue = newValue;
	}

	public Object getOldValue() {
		return oldValue;
	}

	public Object getNewValue() {
		return newValue;
	}
}
