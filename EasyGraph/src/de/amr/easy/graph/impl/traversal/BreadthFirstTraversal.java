package de.amr.easy.graph.impl.traversal;

import static de.amr.easy.graph.api.traversal.TraversalState.COMPLETED;
import static de.amr.easy.graph.api.traversal.TraversalState.UNVISITED;
import static de.amr.easy.graph.api.traversal.TraversalState.VISITED;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Optional;
import java.util.Queue;

import de.amr.easy.graph.api.Graph;

/**
 * Breadth-first traversal of an undirected graph from a given source vertex. After being executed,
 * the distance of each vertex from the source can be queried, as well as the maximal distance of a
 * reachable vertex from the source.
 * 
 * @author Armin Reichert
 */
public class BreadthFirstTraversal extends ObservableGraphTraversal {

	protected Graph<?, ?> graph;
	protected Queue<Integer> q;
	protected int[] distFromSource;
	protected int maxDistance;

	public BreadthFirstTraversal(Graph<?, ?> g) {
		this(g, new ArrayDeque<>());
	}
	
	protected BreadthFirstTraversal(Graph<?, ?> graph, Queue<Integer> q) {
		this.graph = graph;
		this.q = q;
	}

	@Override
	protected void init() {
		super.init();
		q.clear();
		distFromSource = new int[graph.numVertices()];
		Arrays.fill(distFromSource, -1);
		maxDistance = -1;
	}

	@Override
	public void traverseGraph(int source, int target) {
		init();

		q.add(source);
		setState(source, VISITED);
		maxDistance = distFromSource[source] = 0;

		while (!q.isEmpty()) {
			int current = q.poll();
			setState(current, COMPLETED);
			if (current == target) {
				return;
			}
			expand(current);
		}
	}

	private void expand(int current) {
		graph.adj(current).filter(neighbor -> getState(neighbor) == UNVISITED).forEach(neighbor -> {
			q.add(neighbor);
			setState(neighbor, VISITED);
			setParent(neighbor, current);
			distFromSource[neighbor] = distFromSource[current] + 1;
			maxDistance = Math.max(maxDistance, distFromSource[neighbor]);
		});
	}

	/**
	 * The distance of the given vertex from the source.
	 * 
	 * @param v
	 *          some vertex
	 * @return the distance from the source
	 */
	public int getDistance(int v) {
		return distFromSource[v];
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