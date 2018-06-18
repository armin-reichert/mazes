package de.amr.easy.graph.api.event;

import de.amr.easy.graph.api.Edge;

/**
 * Graph event base class.
 * 
 * @author Armin Reichert
 *
 * @param <V>
 *          vertex type
 * @param <E>
 *          edge type
 */
public class GraphEvent<V, E extends Edge> {

	protected final ObservableGraph<V, E> graph;

	public GraphEvent(ObservableGraph<V, E> graph) {
		this.graph = graph;
	}

	public ObservableGraph<V, E> getGraph() {
		return graph;
	}
}