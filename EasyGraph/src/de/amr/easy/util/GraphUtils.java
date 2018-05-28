package de.amr.easy.util;

import de.amr.easy.data.Partition;
import de.amr.easy.graph.api.Edge;
import de.amr.easy.graph.api.Graph;
import de.amr.easy.graph.traversal.BreadthFirstTraversal;

/**
 * Some useful graph methods.
 * 
 * @author Armin Reichert
 */
public class GraphUtils {

	/**
	 * Checks whether a graph contains a cycle.
	 * 
	 * @param <E>
	 *          the edge type of the graph
	 * @param g
	 *          an undirected graph
	 * @return {@code true} if the graph contains a cycle
	 */
	public static <E extends Edge> boolean containsCycle(Graph<E> g) {
		Partition<Integer> p = new Partition<>();
		Iterable<E> edges = g.edgeStream()::iterator;
		for (E edge : edges) {
			int u = edge.either(), v = edge.other(u);
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
	public static <E extends Edge> boolean areConnected(Graph<E> g, int u, int v) {
		BreadthFirstTraversal bfs = new BreadthFirstTraversal(g, u);
		bfs.setStopAt(v);
		bfs.traverseGraph();
		return bfs.getDistance(v) != -1;
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
