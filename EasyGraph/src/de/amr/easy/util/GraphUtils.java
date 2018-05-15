package de.amr.easy.util;

import de.amr.easy.data.Partition;
import de.amr.easy.graph.api.Edge;
import de.amr.easy.graph.api.Graph;

public class GraphUtils {

	public static <E extends Edge> boolean containsCycle(Graph<E> g) {
		Partition<Integer> p = new Partition<>(g.vertexStream().boxed());
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
