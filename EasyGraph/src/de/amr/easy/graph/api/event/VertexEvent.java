package de.amr.easy.graph.api.event;

import de.amr.easy.graph.api.ObservableGraph;

/**
 * Information about a change of a vertex.
 * 
 * @author Armin Reichert
 *
 */
public class VertexEvent {

	private int vertex;
	private Object oldValue;
	private Object newValue;

	public VertexEvent(ObservableGraph<?> graph, int vertex, Object oldValue, Object newValue) {
		this.vertex = vertex;
		this.oldValue = oldValue;
		this.newValue = newValue;
	}

	public int getVertex() {
		return vertex;
	}

	public Object getOldValue() {
		return oldValue;
	}

	public Object getNewValue() {
		return newValue;
	}
}