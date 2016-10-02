package de.amr.easy.graph.event;

import de.amr.easy.graph.api.ObservableGraph;

public class EdgeEvent<V, E> {

	private E edge;

	public EdgeEvent(ObservableGraph<V, E> graph, E edge) {
		this.edge = edge;
	}

	public E getEdge() {
		return edge;
	}
}
