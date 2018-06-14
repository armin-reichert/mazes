package de.amr.easy.graph.api.event;

import de.amr.easy.graph.api.ObservableGraph;

/**
 * Event signaling adding of an edge.
 * 
 * @author Armin Reichert
 */
public class EdgeAddedEvent {

	private final ObservableGraph<?> graph;
	private final int either;
	private final int other;

	public EdgeAddedEvent(ObservableGraph<?> graph, int either, int other) {
		this.graph = graph;
		this.either = either;
		this.other = other;
	}

	public ObservableGraph<?> getGraph() {
		return graph;
	}

	public int getEither() {
		return either;
	}

	public int getOther() {
		return other;
	}
}