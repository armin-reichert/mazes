package de.amr.easy.graph.api.event;

import de.amr.easy.graph.api.Edge;
import de.amr.easy.graph.api.ObservableGraph;

/**
 * Event for edge changes.
 * 
 * @author Armin Reichert
 *
 * @param <E>
 *          the edge type
 */
public class EdgeChangeEvent<E extends Edge> {

	private final ObservableGraph<E> graph;
	private final E edge;
	private final Object oldValue;
	private final Object newValue;

	public EdgeChangeEvent(ObservableGraph<E> graph, E edge, Object oldValue, Object newValue) {
		this.graph = graph;
		this.edge = edge;
		this.oldValue = oldValue;
		this.newValue = newValue;
	}

	public ObservableGraph<E> getGraph() {
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