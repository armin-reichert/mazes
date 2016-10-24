package de.amr.easy.graph.api.event;

import de.amr.easy.graph.api.Edge;
import de.amr.easy.graph.api.ObservableGraph;

/**
 * Event signaling adding of an edge.
 * 
 * @author Armin Reichert
 *
 * @param <V>
 *          vertex type
 * @param <E>
 *          edge type
 */
public class EdgeAddedEvent<V, E extends Edge<V>> {

	private final ObservableGraph<V, E> graph;
	private final E edge;

	public EdgeAddedEvent(ObservableGraph<V, E> graph, E edge) {
		this.graph = graph;
		this.edge = edge;
	}

	public ObservableGraph<V, E> getGraph() {
		return graph;
	}

	public E getEdge() {
		return edge;
	}
}