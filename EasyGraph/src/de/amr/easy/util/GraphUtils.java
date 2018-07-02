package de.amr.easy.util;

import de.amr.easy.data.Partition;
import de.amr.easy.graph.api.Edge;
import de.amr.easy.graph.api.Graph;
import de.amr.easy.graph.impl.traversal.BreadthFirstTraversal;

/**
 * Some useful graph methods.
 * 
 * @author Armin Reichert
 */
public class GraphUtils {

	/**
	 * Checks whether a graph contains a cycle.
	 * 
	 * @param <V>
	 *          vertex label type
	 * @param <E>
	 *          edge label type
	 * @param g
	 *          an undirected graph
	 * @return {@code true} if the graph contains a cycle
	 */
	public static <V, E> boolean containsCycle(Graph<V, E> g) {
		Partition<Integer> p = new Partition<>();
		Iterable<Edge<E>> edges = g.edges()::iterator;
		for (Edge<E> edge : edges) {
			int u = edge.either(), v = edge.other();
			if (p.find(u) == p.find(v)) {
				return true;
			}
			p.union(u, v);
		}
		return false;
	}

	/**
	 * Checks if the given cells are connected by some path.
	 * 
	 * @param u
	 *          a cell
	 * @param v
	 *          a cell
	 * @return {@code true} if there exists a path connecting the given cells
	 */
	public static <V, E> boolean areConnected(Graph<V, E> graph, int u, int v) {
		BreadthFirstTraversal<V, E> bfs = new BreadthFirstTraversal<>(graph);
		bfs.traverseGraph(u, v);
		return bfs.getDistFromSource(v) != -1;
	}

	/**
	 * @param base
	 *          the base of the logarithm
	 * @param n
	 *          a number
	 * @return the next lower integer to the logarithm of the number
	 */
	public static int log(int base, int n) {
		int log = 0;
		for (int pow = 1; pow < n; pow *= base) {
			++log;
		}
		return log;
	}

	/**
	 * @param base
	 *          base of power
	 * @param n
	 *          number
	 * @return next integer which is greater or equals to n and a power of the given base
	 */
	public static int nextPow(int base, int n) {
		int pow = 1;
		while (pow < n) {
			pow *= base;
		}
		return pow;
	}
}