package de.amr.easy.graph.impl.traversal;

import static de.amr.easy.graph.api.traversal.TraversalState.UNVISITED;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.IntStream;

import de.amr.easy.data.Stack;
import de.amr.easy.graph.api.Graph;
import de.amr.easy.graph.api.event.GraphTraversalObserver;
import de.amr.easy.graph.api.traversal.TraversalState;

/**
 * Abstract base class for graph traversals. Stores traversal state and parent link for each vertex
 * and allows to register observers for vertex and edge traversals.
 * 
 * @author Armin Reichert
 */
public abstract class AbstractGraphTraversal {

	protected final Graph<?, ?> graph;
	private final Map<Integer, Integer> parentMap = new HashMap<>();
	private final Map<Integer, TraversalState> stateMap = new HashMap<>();
	private final Set<GraphTraversalObserver> observers = new HashSet<>(5);

	protected AbstractGraphTraversal(Graph<?, ?> graph) {
		this.graph = graph;
	}

	protected boolean isUnvisited(int v) {
		return getState(v) == UNVISITED;
	}

	protected IntStream unvisitedChildren(int v) {
		return graph.adj(v).filter(this::isUnvisited);
	}

	protected void clear() {
		parentMap.clear();
		stateMap.clear();
	}

	public abstract void traverseGraph(int source, int target);

	public void traverseGraph(int source) {
		traverseGraph(source, -1);
	}

	public Iterable<Integer> path(int target) {
		Stack<Integer> path = new Stack<>();
		for (int v = target; v != -1; v = getParent(v)) {
			path.push(v);
		}
		return path;
	}

	public void addObserver(GraphTraversalObserver observer) {
		observers.add(observer);
	}

	public void removeObserver(GraphTraversalObserver observer) {
		observers.remove(observer);
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

	public void edgeTraversed(int either, int other) {
		observers.forEach(observer -> observer.edgeTraversed(either, other));
	}

	public void vertexTraversed(int v, TraversalState oldState, TraversalState newState) {
		observers.forEach(observer -> observer.vertexTraversed(v, oldState, newState));
	}
}