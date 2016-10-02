package de.amr.easy.graph.traversal;

import static de.amr.easy.graph.api.TraversalState.UNVISITED;

import java.util.HashMap;
import java.util.Map;

import de.amr.easy.graph.api.Edge;
import de.amr.easy.graph.api.ObservableGraph;
import de.amr.easy.graph.api.TraversalState;

public abstract class GraphTraversal<V, E extends Edge<V>> implements Runnable {

	protected final ObservableGraph<V, E> graph;
	private final Map<V, V> parentMap = new HashMap<>();
	private final Map<V, TraversalState> stateMap = new HashMap<>();

	public GraphTraversal(ObservableGraph<V, E> graph) {
		this.graph = graph;
	}

	public void clear() {
		parentMap.clear();
		stateMap.clear();
	}

	public TraversalState getState(V v) {
		TraversalState state = stateMap.get(v);
		return state == null ? UNVISITED : state;
	}

	public void setState(V v, TraversalState state) {
		TraversalState oldState = stateMap.get(v);
		stateMap.put(v, state);
		graph.fireVertexChange(v, oldState, state);
	}

	public V getParent(V v) {
		return parentMap.get(v);
	}

	public void setParent(V v, V parent) {
		parentMap.put(v, parent);
	}
}
