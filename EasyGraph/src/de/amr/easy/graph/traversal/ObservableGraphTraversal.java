package de.amr.easy.graph.traversal;

import static de.amr.easy.graph.api.TraversalState.UNVISITED;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.IntStream;

import de.amr.easy.graph.api.PathFinder;
import de.amr.easy.graph.api.TraversalState;
import de.amr.easy.graph.api.event.GraphTraversalListener;

/**
 * Base class for graph traversals. Stores traversal state and parent vertex of each vertex and
 * allows to register observers for vertex and edge visits.
 * 
 * @author Armin Reichert
 */
public abstract class ObservableGraphTraversal implements PathFinder {

	protected final Map<Integer, Integer> parentMap = new HashMap<>();
	protected final Map<Integer, TraversalState> stateMap = new HashMap<>();
	protected final Set<GraphTraversalListener> observers = new HashSet<>(3);

	public abstract void traverseGraph();

	public void clear() {
		parentMap.clear();
		stateMap.clear();
	}

	public TraversalState getState(int v) {
		return stateMap.containsKey(v) ? stateMap.get(v) : UNVISITED;
	}

	public void setState(int v, TraversalState newState) {
		TraversalState oldState = getState(v);
		stateMap.put(v, newState);
		vertexTouched(v, oldState, newState);
	}

	public int getParent(int v) {
		return parentMap.containsKey(v) ? parentMap.get(v) : -1;
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

	protected void edgeTouched(int u, int v) {
		observers.forEach(observer -> observer.edgeTouched(u, v));
	}

	protected void vertexTouched(int u, TraversalState oldState, TraversalState newState) {
		observers.forEach(observer -> observer.vertexTouched(u, oldState, newState));
	}

	// {PathFinder} interface

	@Override
	public IntStream findPath(int target) {
		if (getParent(target) == -1) {
			return IntStream.empty();
		}
		List<Integer> path = new LinkedList<>();
		for (int v = target; v != -1; v = getParent(v)) {
			path.add(0, v);
		}
		return path.stream().mapToInt(Integer::intValue);
	}
}