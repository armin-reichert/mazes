package de.amr.easy.util;

import de.amr.easy.data.Partition;
import de.amr.easy.graph.api.Edge;
import de.amr.easy.graph.api.Graph;

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

}
