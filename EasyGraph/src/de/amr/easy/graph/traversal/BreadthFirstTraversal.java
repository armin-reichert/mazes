package de.amr.easy.graph.traversal;

import static de.amr.easy.graph.api.TraversalState.COMPLETED;
import static de.amr.easy.graph.api.TraversalState.VISITED;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Optional;
import java.util.Queue;

import de.amr.easy.graph.api.Graph;

/**
 * Breadth-first-traversal of an undirected graph from a given source vertex. After being executed,
 * the distance of each vertex from the source can be queried, as well as the maximal distance of a
 * reachable vertex from the source.
 * <p>
 * During the traversal, events are fired which can be processed by a listener, for example an
 * animation.
 * 
 * @author Armin Reichert
 * 
 * @param <G>
 *          graph type
 */
public class BreadthFirstTraversal<G extends Graph<?>> extends AbstractGraphTraversal {

	protected final Queue<Integer> q;
	protected final int[] distance;
	private int maxDistance;

	protected BreadthFirstTraversal(G graph, Queue<Integer> queue) {
		super(graph);
		this.q = queue;
		this.distance = new int[graph.vertexCount()];
		clear();
	}

	public BreadthFirstTraversal(G graph) {
		this(graph, new ArrayDeque<>());
	}

	@Override
	protected void clear() {
		super.clear();
		q.clear();
		Arrays.fill(distance, -1);
		maxDistance = -1;
	}

	@Override
	public boolean inQ(int vertex) {
		return q.contains(vertex);
	}

	@Override
	public void traverseGraph(int source, int target) {
		clear();
		visit(source, -1);
		while (!q.isEmpty()) {
			if (q.peek() == target) {
				break;
			}
			int current = q.poll();
			childrenInQueuingOrder(current).forEach(child -> visit(child, current));
			setState(current, COMPLETED);
		}
	}

	private void visit(int v, int parent) {
		q.add(v);
		setState(v, VISITED);
		setParent(v, parent);
		int d = parent != -1 ? distance[parent] + 1 : 0;
		if (d > maxDistance) {
			maxDistance = d;
		}
		distance[v] = d;
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
		return distance[v];
	}

	/**
	 * Returns the maximum distance encountered in this traversal.
	 * 
	 * @return the maximum distance
	 */
	public int getMaxDistance() {
		return maxDistance;
	}

	/**
	 * Returns a vertex with maximum distance encountered in this traversal.
	 * 
	 * @return a vertex with maximum distance or empty
	 */
	public Optional<Integer> getMaxDistanceVertex() {
		return graph.vertices().boxed().max(Comparator.comparing(this::getDistance));
	}
}