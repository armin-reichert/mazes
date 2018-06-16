package de.amr.easy.graph.traversal;

import static de.amr.easy.graph.api.TraversalState.UNVISITED;

import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.IntStream;

import de.amr.easy.graph.api.Graph;
import de.amr.easy.graph.api.ObservableGraphTraversal;
import de.amr.easy.graph.api.PathFinder;
import de.amr.easy.graph.api.TraversalState;
import de.amr.easy.graph.api.event.GraphTraversalObserver;

/**
 * Base class for graph traversals. Stores traversal state and parent vertex of each vertex and
 * allows to register observers for vertex and edge visits.
 * 
 * @author Armin Reichert
 * 
 * @param <G>
 *          the graph type
 */
public abstract class AbstractGraphTraversal implements ObservableGraphTraversal, PathFinder {

	protected final Map<Integer, Integer> parentMap = new HashMap<>();

	protected final Map<Integer, TraversalState> stateMap = new HashMap<>();

	protected final Set<GraphTraversalObserver> observers = new HashSet<>(3);

	protected final Graph<?> graph;

	public AbstractGraphTraversal(Graph<?> graph) {
		this.graph = graph;
	}

	/**
	 * @return the traversed graph
	 */
	public Graph<?> getGraph() {
		return graph;
	}

	/**
	 * @param v
	 *          a vertex
	 * @return the children of this vertex in the order they will be added to the search queue
	 */
	protected IntStream childrenInQueuingOrder(int v) {
		return graph.adjVertices(v).filter(child -> getState(child) == UNVISITED);
	}

	protected void clear() {
		parentMap.clear();
		stateMap.clear();
	}

	@Override
	public TraversalState getState(int v) {
		return stateMap.containsKey(v) ? stateMap.get(v) : UNVISITED;
	}

	protected void setState(int v, TraversalState newState) {
		TraversalState oldState = getState(v);
		stateMap.put(v, newState);
		vertexVisited(v, oldState, newState);
	}

	@Override
	public int getParent(int v) {
		return parentMap.containsKey(v) ? parentMap.get(v) : -1;
	}

	public void setParent(int v, int parent) {
		parentMap.put(v, parent);
	}

	@Override
	public void addObserver(GraphTraversalObserver observer) {
		observers.add(observer);
	}

	@Override
	public void removeObserver(GraphTraversalObserver observer) {
		observers.remove(observer);
	}

	@Override
	public void edgeTouched(int u, int v) {
		observers.forEach(observer -> observer.edgeTraversed(u, v));
	}

	@Override
	public void vertexVisited(int u, TraversalState oldState, TraversalState newState) {
		observers.forEach(observer -> observer.vertexTraversed(u, oldState, newState));
	}

	@Override
	public IntStream findPath(int target) {
		if (getParent(target) == -1) {
			return IntStream.empty();
		}
		ArrayDeque<Integer> path = new ArrayDeque<>();
		for (int v = target; v != -1; v = getParent(v)) {
			path.addFirst(v);
		}
		return path.stream().mapToInt(Integer::intValue);
	}
}