package de.amr.easy.graph.alg.traversal;

import static de.amr.easy.graph.api.TraversalState.UNVISITED;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import de.amr.easy.graph.api.TraversalState;
import de.amr.easy.graph.api.event.GraphTraversalListener;

/**
 * Base class for graph traversals. Stores traversal state of vertices and allows to register
 * observers for vertex and edge traversal.
 * 
 * @author Armin Reichert
 */
public abstract class AbstractGraphTraversal {

	protected final Map<Integer, Integer> parentMap = new HashMap<>();
	protected final Map<Integer, TraversalState> stateMap = new HashMap<>();
	protected final Set<GraphTraversalListener> observers = new HashSet<>(3);

	public abstract void traverseGraph();

	public void clear() {
		parentMap.clear();
		stateMap.clear();
	}

	public TraversalState getState(int v) {
		TraversalState state = stateMap.get(v);
		return state == null ? UNVISITED : state;
	}

	public void setState(int v, TraversalState newState) {
		TraversalState oldState = stateMap.get(v);
		stateMap.put(v, newState);
		observers.forEach(observer -> observer.vertexTouched(v, oldState, newState));
	}

	public int getParent(int v) {
		return parentMap.get(v);
	}

	public void setParent(int v, int parent) {
		parentMap.put(v, parent);
	}

	public void addObserver(GraphTraversalListener observer) {
		observers.add(observer);
	}

	public void removeObserver(GraphTraversalListener observer) {
		observers.remove(observer);
	}
}