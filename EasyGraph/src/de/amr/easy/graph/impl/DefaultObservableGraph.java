package de.amr.easy.graph.impl;

import java.util.HashSet;
import java.util.Set;
import java.util.function.BiFunction;

import de.amr.easy.graph.api.Edge;
import de.amr.easy.graph.api.event.EdgeEvent;
import de.amr.easy.graph.api.event.GraphObserver;
import de.amr.easy.graph.api.event.ObservableGraph;

/**
 * Adjacency set based implementation of an undirected, observable graph.
 * 
 * @author Armin Reichert
 * 
 * @param <V>
 *          vertex type
 * @param <E>
 *          edge type
 */
public class DefaultObservableGraph<V, E extends Edge> extends DefaultGraph<E> implements ObservableGraph<V, E> {

	private Set<GraphObserver<V, E>> observers = new HashSet<>();
	private boolean eventsEnabled;

	public DefaultObservableGraph(BiFunction<Integer, Integer, E> fnEdgeFactory) {
		super(fnEdgeFactory);
		eventsEnabled = true;
	}

	@Override
	public void addGraphObserver(GraphObserver<V, E> o) {
		observers.add(o);
	}

	@Override
	public void removeGraphObserver(GraphObserver<V, E> o) {
		observers.remove(o);
	}

	@Override
	public void setEventsEnabled(boolean enabled) {
		eventsEnabled = enabled;
	}

	@Override
	public void addVertex(int vertex) {
		super.addVertex(vertex);
		if (eventsEnabled) {
			observers.forEach(o -> o.graphChanged(this));
		}
	}

	@Override
	public void addEdge(int v, int w) {
		super.addEdge(v, w);
		if (eventsEnabled) {
			observers.forEach(o -> o.edgeAdded(new EdgeEvent<>(this, v, w)));
		}
	}

	@Override
	public void removeEdge(int v, int w) {
		edge(v, w).ifPresent(edge -> {
			super.removeEdge(v, w);
			if (eventsEnabled) {
				observers.forEach(o -> o.edgeRemoved(new EdgeEvent<>(this, v, w)));
			}
		});
	}

	@Override
	public void removeEdges() {
		super.removeEdges();
		if (eventsEnabled) {
			observers.forEach(o -> o.graphChanged(this));
		}
	}
}
