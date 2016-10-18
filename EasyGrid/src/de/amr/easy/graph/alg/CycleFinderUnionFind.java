package de.amr.easy.graph.alg;

import java.util.Iterator;

import de.amr.easy.graph.api.DataGraph;
import de.amr.easy.graph.api.Edge;
import de.amr.easy.graph.api.TraversalState;
import de.amr.easy.grid.ds.Partition;

public class CycleFinderUnionFind<V, E extends Edge<V>> {

	private final Partition<V> partition = new Partition<>();
	private boolean cycleDetected = false;

	public boolean isCycleDetected() {
		return cycleDetected;
	}

	public CycleFinderUnionFind(DataGraph<V, E, TraversalState> g, V start) {
		for (Iterator<E> edges = g.edgeStream().iterator(); edges.hasNext();) {
			E edge = edges.next();
			V either = edge.either(), other = edge.other(either);
			if (partition.find(either) == partition.find(other)) {
				cycleDetected = true;
				break;
			}
			partition.union(partition.find(either), partition.find(other));
		}
	}

}