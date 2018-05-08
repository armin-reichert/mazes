package de.amr.easy.graph.alg.traversal;

import static de.amr.easy.graph.api.TraversalState.COMPLETED;
import static de.amr.easy.graph.api.TraversalState.UNVISITED;
import static de.amr.easy.graph.api.TraversalState.VISITED;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import de.amr.easy.graph.api.Edge;
import de.amr.easy.graph.api.Graph;
import de.amr.easy.graph.api.PathFinder;

/**
 * Breadth-first-traversal of an undirected graph from a given source vertex. After being executed,
 * the distance of each vertex from the source can be queried, as well as the maximal distance of a
 * reachable vertex from the source.
 * <p>
 * Implements the {@link PathFinder} interface such that the traversal state of each vertex can be
 * queried and a path from the source to any target vertex can be asked for.
 * 
 * <p>
 * During the traversal, events are fired which can be processed by a listener, for example an
 * animation.
 * 
 * @param <E>
 *          the edge type
 * 
 * @author Armin Reichert
 */
public class BreadthFirstTraversal<E extends Edge> extends AbstractGraphTraversal<E> implements PathFinder {

	private final Graph<E> graph;
	private final int source;
	private final Queue<Integer> q;
	private final int[] distances;
	private int maxDistance = -1;
	private int farest = -1;
	private int stopAt = -1;

	public BreadthFirstTraversal(Graph<E> graph, int source) {
		this.graph = graph;
		this.source = source;
		q = new ArrayDeque<>();
		distances = new int[graph.vertexCount()];
		Arrays.fill(distances, -1);
	}

	public void setStopAt(int vertex) {
		this.stopAt = vertex;
	}

	@Override
	public void clear() {
		super.clear();
		q.clear();
		Arrays.fill(distances, -1);
		maxDistance = -1;
		farest = -1;
	}

	@Override
	public void traverseGraph() {
		clear();
		visit(source, -1, 0);
		q.add(source);
		while (!q.isEmpty()) {
			int current = q.poll();
			graph.adjVertices(current).filter(v -> getState(v) == UNVISITED).forEach(neighbor -> {
				visit(neighbor, current, getDistance(current) + 1);
				q.add(neighbor);
			});
			setState(current, COMPLETED);
			if (current == stopAt) {
				break;
			}
		}
	}

	private void visit(int v, int parent, int distance) {
		distances[v] = distance;
		if (distance > maxDistance) {
			maxDistance = distance;
			farest = v;
		}
		setState(v, VISITED);
		setParent(v, parent);
		if (parent != -1) {
			observers.forEach(observer -> observer.edgeTouched(parent, v));
		}
	}

	/**
	 * The distance of the given vertex from the source.
	 * 
	 * @param v
	 *          some vertex
	 * @return the distance from the source
	 */
	public int getDistance(int v) {
		return distances[v];
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
	 * @return a vertex with maximal distance from the source
	 */
	public int getMaxDistanceVertex() {
		return farest;
	}

	// {PathFinder} interface

	@Override
	public Iterable<Integer> findPath(int target) {
		if (getDistance(target) == -1) {
			return Collections.emptyList();
		}
		List<Integer> path = new LinkedList<>();
		for (int vertex = target; vertex != -1; vertex = getParent(vertex)) {
			path.add(0, vertex);
		}
		return path;
	}
}
