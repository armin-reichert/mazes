package de.amr.easy.graph.alg;

import java.util.Iterator;

import de.amr.easy.datastruct.Partition;
import de.amr.easy.graph.api.Edge;
import de.amr.easy.graph.api.Graph;

public class CycleFinderUnionFind<V, E extends Edge<V>> {

	private final Partition<V> partition = new Partition<>();
	private boolean cycleFound = false;

	public boolean isCycleFound() {
		return cycleFound;
	}

	public CycleFinderUnionFind(Graph<V, E> g, V start) {
		for (Iterator<E> edges = g.edgeStream().iterator(); edges.hasNext();) {
			E edge = edges.next();
			V either = edge.either(), other = edge.other(either);
			if (partition.find(either) == partition.find(other)) {
				cycleFound = true;
				break;
			}
			partition.union(partition.find(either), partition.find(other));
		}
	}
}