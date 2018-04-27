package de.amr.easy.graph.api.event;

import de.amr.easy.graph.api.Edge;
import de.amr.easy.graph.api.ObservableGraph;

/**
 * Event signaling removal of an edge.
 * 
 * @author Armin Reichert
 *
 * @param <E>
 *          edge type
 */
public class EdgeRemovedEvent<E extends Edge> {

	private final ObservableGraph<E> graph;
	private final E edge;

	public EdgeRemovedEvent(ObservableGraph<E> graph, E edge) {
		this.graph = graph;
		this.edge = edge;
	}

	public ObservableGraph<E> getGraph() {
		return graph;
	}

	public E getEdge() {
		return edge;
	}
}