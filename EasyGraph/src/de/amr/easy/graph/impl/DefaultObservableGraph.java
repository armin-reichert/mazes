package de.amr.easy.graph.impl;

import java.util.HashSet;
import java.util.Set;
import java.util.function.BiFunction;

import de.amr.easy.graph.api.Edge;
import de.amr.easy.graph.api.event.EdgeEvent;
import de.amr.easy.graph.api.event.GraphObserver;
import de.amr.easy.graph.api.event.ObservableGraph;
import de.amr.easy.graph.api.event.VertexEvent;

/**
 * Adjacency set based implementation of an undirected, observable graph.
 * 
 * @author Armin Reichert
 * 
 * @param <E>
 *          edge type
 */
public class DefaultObservableGraph<E extends Edge> extends DefaultGraph<E> implements ObservableGraph<E> {

	private Set<GraphObserver> listeners = new HashSet<>();
	private boolean listeningSuspended = false;

	public DefaultObservableGraph(BiFunction<Integer, Integer, E> fnEdgeFactory) {
		super(fnEdgeFactory);
		this.listeningSuspended = false;
	}

	@Override
	public void addGraphObserver(GraphObserver listener) {
		listeners.add(listener);
	}

	@Override
	public void removeGraphObserver(GraphObserver listener) {
		listeners.remove(listener);
	}

	@Override
	public void setEventsEnabled(boolean enabled) {
		listeningSuspended = enabled;
	}

	protected void fireVertexChange(int vertex, Object oldValue, Object newValue) {
		if (!listeningSuspended) {
			for (GraphObserver listener : listeners) {
				listener.vertexChanged(new VertexEvent(this, vertex, oldValue, newValue));
			}
		}
	}

	protected void fireEdgeChange(Edge edge, Object oldValue, Object newValue) {
		if (!listeningSuspended) {
			for (GraphObserver listener : listeners) {
				listener.edgeChanged(new EdgeEvent(this, edge.either(), edge.other(), oldValue, newValue));
			}
		}
	}

	protected void fireGraphChange(ObservableGraph<E> graph) {
		if (!listeningSuspended) {
			for (GraphObserver listener : listeners) {
				listener.graphChanged(graph);
			}
		}
	}

	@Override
	public void addVertex(int vertex) {
		super.addVertex(vertex);
		fireVertexChange(vertex, null, vertex);
	}

	@Override
	public void addEdge(int v, int w) {
		super.addEdge(v, w);
		edge(v, w).ifPresent(edge -> fireEdgeChange(edge, null, edge));
	}

	@Override
	public void removeEdge(int v, int w) {
		edge(v, w).ifPresent(edge -> {
			super.removeEdge(v, w);
			fireEdgeChange(edge, edge, null);
		});
	}

	@Override
	public void removeEdges() {
		super.removeEdges();
		fireGraphChange(this);
	}
}
