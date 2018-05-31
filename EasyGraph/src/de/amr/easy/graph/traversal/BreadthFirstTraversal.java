package de.amr.easy.graph.traversal;

import static de.amr.easy.graph.api.TraversalState.COMPLETED;
import static de.amr.easy.graph.api.TraversalState.UNVISITED;
import static de.amr.easy.graph.api.TraversalState.VISITED;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Queue;

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
 * @author Armin Reichert
 */
public class BreadthFirstTraversal extends ObservableGraphTraversal implements PathFinder {

	private final Queue<Integer> q;
	private final Graph<?> graph;
	private final int source;
	private final int[] distances;
	private int maxDistance;
	private int farest;
	private int stopAt;

	public BreadthFirstTraversal(Graph<?> graph, int source) {
		this.q = new ArrayDeque<>();
		this.graph = graph;
		this.source = source;
		this.distances = new int[graph.vertexCount()];
		clear();
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
		stopAt = -1;
	}

	@Override
	public void traverseGraph() {
		clear();
		visit(source, -1);
		while (!q.isEmpty()) {
			int current = q.poll();
			if (current == stopAt) {
				break;
			}
			///*@formatter:off*/
			graph.adjVertices(current)
				.filter(v -> getState(v) == UNVISITED)
				.forEach(child -> {
					visit(child, current);
				});
			/*@formatter:on*/
			setState(current, COMPLETED);
		}
	}

	private void visit(int v, int parent) {
		setState(v, VISITED);
		setParent(v, parent);
		int d = parent != -1 ? distances[parent] + 1 : 0;
		distances[v] = d;
		if (d > maxDistance) {
			maxDistance = d;
			farest = v;
		}
		q.add(v);
		if (parent != -1) {
			edgeTouched(parent, v);
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
}