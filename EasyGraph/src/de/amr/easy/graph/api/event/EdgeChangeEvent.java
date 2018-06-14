package de.amr.easy.graph.api.event;

import de.amr.easy.graph.api.ObservableGraph;

/**
 * Event for edge changes.
 * 
 * @author Armin Reichert
 */
public class EdgeChangeEvent {

	private final ObservableGraph<?> graph;
	private final int either;
	private final int other;
	private final Object oldValue;
	private final Object newValue;

	public EdgeChangeEvent(ObservableGraph<?> graph, int either, int other, Object oldValue, Object newValue) {
		this.graph = graph;
		this.either = either;
		this.other = other;
		this.oldValue = oldValue;
		this.newValue = newValue;
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

	public Object getOldValue() {
		return oldValue;
	}

	public Object getNewValue() {
		return newValue;
	}
}