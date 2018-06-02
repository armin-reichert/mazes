package de.amr.easy.graph.traversal;

import static de.amr.easy.graph.api.TraversalState.COMPLETED;
import static de.amr.easy.graph.api.TraversalState.UNVISITED;
import static de.amr.easy.graph.api.TraversalState.VISITED;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Queue;

import de.amr.easy.graph.api.Graph;
import de.amr.easy.graph.api.PathFinder;
import de.amr.easy.graph.api.GraphTraversal;

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
public class BreadthFirstTraversal extends AbstractGraphTraversal implements GraphTraversal {

	protected final Queue<Integer> q;
	protected final int[] distanceMap;
	protected int maxDistance;
	protected int farest;
	protected int target;

	protected BreadthFirstTraversal(Graph<?> graph, Queue<Integer> queue) {
		super(graph);
		this.q = queue;
		this.distanceMap = new int[graph.vertexCount()];
		clear();
	}

	public BreadthFirstTraversal(Graph<?> graph) {
		this(graph, new ArrayDeque<>());
	}

	@Override
	protected void clear() {
		super.clear();
		q.clear();
		Arrays.fill(distanceMap, -1);
		maxDistance = -1;
		farest = -1;
		target = -1;
	}

	@Override
	public void traverseGraph(int source, int target) {
		clear();
		visit(source, -1);
		while (!q.isEmpty()) {
			int current = q.poll();
			if (current == target) {
				break;
			}
			graph.adjVertices(current).filter(v -> getState(v) == UNVISITED).forEach(child -> visit(child, current));
			setState(current, COMPLETED);
		}
	}
	
	@Override
	public boolean inQ(int vertex) {
		return q.contains(vertex);
	}

	private void visit(int v, int parent) {
		setState(v, VISITED);
		setParent(v, parent);
		int d = parent != -1 ? distanceMap[parent] + 1 : 0;
		distanceMap[v] = d;
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
		return distanceMap[v];
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