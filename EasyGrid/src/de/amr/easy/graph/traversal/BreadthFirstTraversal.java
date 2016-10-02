package de.amr.easy.graph.traversal;

import static de.amr.easy.graph.api.TraversalState.COMPLETED;
import static de.amr.easy.graph.api.TraversalState.UNVISITED;
import static de.amr.easy.graph.api.TraversalState.VISITED;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import de.amr.easy.graph.api.ObservableGraph;
import de.amr.easy.graph.api.SingleSourcePathFinder;

/**
 * Breadth-first-traversal of an observable, undirected graph from a given source vertex. After
 * being executed, the distance of each vertex from the source can be queried, additionally the
 * maximum distance from the source.
 * <p>
 * Implements the {@link SingleSourcePathFinder} interface such that the traversal state of each
 * vertex can be queried and a path from the source to any target vertex can be asked for.
 * 
 * <p>
 * During the traversal, a number of graph events are fired which can be processed by a listener,
 * for example an animation.
 * 
 * @param <V>
 *          the vertex type
 * @param <E>
 *          the edge type
 * 
 * @author Armin Reichert
 */
public class BreadthFirstTraversal<V, E> extends GraphTraversal<V, E> implements SingleSourcePathFinder<V> {

	private final V source;
	private final Map<V, Integer> distanceFromSource = new HashMap<>();
	private int maxDistance = -1;
	private V maxDistanceVertex;

	public BreadthFirstTraversal(ObservableGraph<V, E> graph, V source) {
		super(graph);
		this.source = source;
	}

	@Override
	public void run() {
		clear();
		distanceFromSource.clear();
		maxDistance = -1;
		maxDistanceVertex = null;
		Queue<V> queue = new LinkedList<>();
		visit(source, null, 0);
		queue.add(source);
		while (!queue.isEmpty()) {
			V vertex = queue.poll();
			for (V neighbor : graph.adjVertices(vertex)) {
				if (getState(neighbor) == UNVISITED) {
					visit(neighbor, vertex, getDistance(vertex) + 1);
					queue.add(neighbor);
				}
			}
			setState(vertex, COMPLETED);
		}
	}

	private void visit(V vertex, V parent, int distance) {
		distanceFromSource.put(vertex, distance);
		if (distance > maxDistance) {
			maxDistance = distance;
			maxDistanceVertex = vertex;
		}
		setState(vertex, VISITED);
		setParent(vertex, parent);
		if (parent != null) {
			graph.edge(parent, vertex).ifPresent(edge -> graph.fireEdgeChange(edge, UNVISITED, VISITED));
		}
	}

	/**
	 * The distance of the given vertex from the source.
	 * 
	 * @param v
	 *          some vertex
	 * @return the distance from the source
	 */
	public int getDistance(V v) {
		return distanceFromSource.containsKey(v) ? distanceFromSource.get(v) : -1;
	}

	/**
	 * The maximum distance from the source to any reachable vertex.
	 * 
	 * @return the maximum distance
	 */
	public int getMaxDistance() {
		return maxDistance;
	}

	/**
	 * @return a vertex whose distance id maximal from the source
	 */
	public V getMaxDistanceVertex() {
		return maxDistanceVertex;
	}

	// PathFinder implementation

	@Override
	public Iterable<V> findPath(V target) {
		if (target == null) {
			throw new NullPointerException("Target is null");
		}
		if (getDistance(target) == -1) {
			return Collections.emptyList();
		}
		List<V> path = new LinkedList<>();
		for (V vertex = target; vertex != null; vertex = getParent(vertex)) {
			path.add(0, vertex);
		}
		return path;
	}
}
