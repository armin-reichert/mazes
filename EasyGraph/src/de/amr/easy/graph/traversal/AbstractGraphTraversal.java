package de.amr.easy.graph.traversal;

import static de.amr.easy.graph.api.TraversalState.UNVISITED;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.IntStream;

import de.amr.easy.data.Stack;
import de.amr.easy.graph.api.Graph;
import de.amr.easy.graph.api.GraphTraversal;
import de.amr.easy.graph.api.TraversalState;
import de.amr.easy.graph.api.event.GraphTraversalObserver;

/**
 * Abstract base class for graph traversals. Stores traversal state and parent link for each vertex
 * and allows to register observers for vertex and edge traversals.
 * 
 * @author Armin Reichert
 */
public abstract class AbstractGraphTraversal implements GraphTraversal {

	protected final Graph<?> graph;
	protected final Map<Integer, Integer> parentMap = new HashMap<>();
	protected final Map<Integer, TraversalState> stateMap = new HashMap<>();
	protected final Set<GraphTraversalObserver> observers = new HashSet<>(5);

	protected AbstractGraphTraversal(Graph<?> graph) {
		this.graph = graph;
	}

	/**
	 * Builds and returns the path from the source of this traversal to the given target vertex.
	 * 
	 * @param target
	 *          target vertex
	 * @return path from source to target
	 */
	public Iterable<Integer> path(int target) {
		Stack<Integer> path = new Stack<>();
		for (int v = target; v != -1; v = getParent(v)) {
			path.push(v);
		}
		return path;
	}

	/**
	 * Adds an observer.
	 * 
	 * @param observer
	 *          traversal observer
	 */
	public void addObserver(GraphTraversalObserver observer) {
		observers.add(observer);
	}

	/**
	 * Removes an observer.
	 * 
	 * @param observer
	 *          traversal observer
	 */
	public void removeObserver(GraphTraversalObserver observer) {
		observers.remove(observer);
	}

	protected void setState(int v, TraversalState newState) {
		TraversalState oldState = getState(v);
		stateMap.put(v, newState);
		vertexTraversed(v, oldState, newState);
	}

	/**
	 * @param v
	 *          a vertex
	 * @return the children of this vertex in the order they will be added to the search queue
	 */
	protected IntStream childrenInQueuingOrder(int v) {
		return graph.adj(v).filter(child -> getState(child) == UNVISITED);
	}

	@Override
	public TraversalState getState(int v) {
		return stateMap.containsKey(v) ? stateMap.get(v) : UNVISITED;
	}

	@Override
	public int getParent(int v) {
		return parentMap.containsKey(v) ? parentMap.get(v) : -1;
	}

	@Override
	public void edgeTraversed(int either, int other) {
		observers.forEach(observer -> observer.edgeTraversed(either, other));
	}

	@Override
	public void vertexTraversed(int v, TraversalState oldState, TraversalState newState) {
		observers.forEach(observer -> observer.vertexTraversed(v, oldState, newState));
	}
}