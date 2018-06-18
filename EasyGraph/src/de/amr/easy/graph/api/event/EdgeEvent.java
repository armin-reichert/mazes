package de.amr.easy.graph.api.event;

import de.amr.easy.graph.api.Edge;

/**
 * Event for edge related changes of a graph.
 * 
 * @author Armin Reichert
 */
public class EdgeEvent<V,E extends Edge> extends GraphEvent<V,E> {

	private final int either;
	private final int other;

	public EdgeEvent(ObservableGraph<V, E> graph, int either, int other) {
		super(graph);
		this.either = either;
		this.other = other;
	}

	public int getEither() {
		return either;
	}

	public int getOther() {
		return other;
	}
}