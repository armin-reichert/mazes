package de.amr.easy.graph.alg.traversal;

import static de.amr.easy.graph.api.TraversalState.UNVISITED;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import de.amr.easy.graph.api.Edge;
import de.amr.easy.graph.api.Graph;
import de.amr.easy.graph.api.TraversalState;
import de.amr.easy.graph.api.event.GraphTraversalListener;

/**
 * Base class for graph traversals. Stores traversal state of vertices and allows to register
 * observers for vertex and edge traversal.
 * 
 * @author Armin Reichert
 *
 * @param <V>
 *          vertex type
 * @param <E>
 *          edge type
 */
public abstract class GraphTraversal<V, E extends Edge<V>> implements Runnable {

	protected final Graph<V, E> graph;
	protected final Map<V, V> parentMap = new HashMap<>();
	protected final Map<V, TraversalState> stateMap = new HashMap<>();
	protected final Set<GraphTraversalListener<V>> observers = new HashSet<>(3);

	public GraphTraversal(Graph<V, E> graph) {
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

	public void setState(V v, TraversalState newState) {
		TraversalState oldState = stateMap.get(v);
		stateMap.put(v, newState);
		observers.forEach(observer -> observer.vertexTouched(v, oldState, newState));
	}

	public V getParent(V v) {
		return parentMap.get(v);
	}

	public void setParent(V v, V parent) {
		parentMap.put(v, parent);
	}

	public void addObserver(GraphTraversalListener<V> observer) {
		observers.add(observer);
	}

	public void removeObserver(GraphTraversalListener<V> observer) {
		observers.remove(observer);
	}
}