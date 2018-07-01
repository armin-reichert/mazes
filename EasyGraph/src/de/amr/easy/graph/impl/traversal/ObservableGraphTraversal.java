package de.amr.easy.graph.impl.traversal;

import static de.amr.easy.graph.api.traversal.TraversalState.UNVISITED;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import de.amr.easy.graph.api.event.GraphTraversalObserver;
import de.amr.easy.graph.api.traversal.TraversalState;

/**
 * Abstract base class for graph traversals. Stores traversal state and parent link for each vertex
 * and allows to register observers for vertex and edge traversals.
 * 
 * @author Armin Reichert
 */
public abstract class ObservableGraphTraversal {

	private final Map<Integer, Integer> parentMap = new HashMap<>();

	private final Map<Integer, TraversalState> stateMap = new HashMap<>();

	private final Set<GraphTraversalObserver> observers = new HashSet<>(5);

	/**
	 * Initializes the traversal such that {@link #traverseGraph(int, int)} starts in a clean state.
	 */
	protected void init() {
		parentMap.clear();
		stateMap.clear();
	}

	/**
	 * Traverses the graph starting from the given source until the target is reached.
	 * 
	 * @param source
	 *          source vertex
	 * @param target
	 *          target vertex
	 */
	public abstract void traverseGraph(int source, int target);

	/**
	 * Traverses the graph starting from the given source until all reachable vertices are visited.
	 * 
	 * @param source
	 *          source vertex
	 */
	public void traverseGraph(int source) {
		traverseGraph(source, -1);
	}

	/**
	 * @param target
	 *          target vertex
	 * @return path from source to target vertex
	 */
	public List<Integer> path(int target) {
		List<Integer> path = new ArrayList<>();
		for (int v = target; v != -1; v = getParent(v)) {
			path.add(v);
		}
		Collections.reverse(path);
		return path;
	}

	protected void setState(int v, TraversalState newState) {
		TraversalState oldState = getState(v);
		stateMap.put(v, newState);
		vertexTraversed(v, oldState, newState);
	}

	public TraversalState getState(int v) {
		return stateMap.containsKey(v) ? stateMap.get(v) : UNVISITED;
	}

	protected void setParent(int child, int parent) {
		parentMap.put(child, parent);
		if (parent != -1) {
			edgeTraversed(parent, child);
		}
	}

	public int getParent(int v) {
		return parentMap.containsKey(v) ? parentMap.get(v) : -1;
	}

	// Observer related stuff

	public void addObserver(GraphTraversalObserver observer) {
		observers.add(observer);
	}

	public void removeObserver(GraphTraversalObserver observer) {
		observers.remove(observer);
	}

	public void edgeTraversed(int either, int other) {
		observers.forEach(observer -> observer.edgeTraversed(either, other));
	}

	public void vertexTraversed(int v, TraversalState oldState, TraversalState newState) {
		observers.forEach(observer -> observer.vertexTraversed(v, oldState, newState));
	}
}