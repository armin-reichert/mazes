package de.amr.easy.graph.alg.traversal;

import static de.amr.easy.graph.api.TraversalState.COMPLETED;
import static de.amr.easy.graph.api.TraversalState.UNVISITED;
import static de.amr.easy.graph.api.TraversalState.VISITED;

import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.stream.IntStream;

import de.amr.easy.graph.api.Graph;
import de.amr.easy.graph.api.PathFinder;

public class BestFirstTraversal extends AbstractGraphTraversal implements PathFinder {

	private final Graph<?> graph;
	private final int source;
	private final PriorityQueue<Integer> q;
	private final int[] distances;
	private int maxDistance;
	private int farest;
	private int stopAt;

	public BestFirstTraversal(Graph<?> graph, int source, Comparator<Integer> vertexComparator) {
		this.graph = graph;
		this.source = source;
		q = new PriorityQueue<>(vertexComparator);
		distances = new int[graph.vertexCount()];
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


}
