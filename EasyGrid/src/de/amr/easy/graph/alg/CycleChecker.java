package de.amr.easy.graph.alg;

import java.util.function.Predicate;

import de.amr.easy.data.Partition;
import de.amr.easy.graph.api.Edge;
import de.amr.easy.graph.api.Graph;

public class CycleChecker<E extends Edge> implements Predicate<Graph<E>> {

	@Override
	public boolean test(Graph<E> g) {
		Partition<Integer> connected = new Partition<>();
		for (E edge : (Iterable<E>) g.edgeStream()::iterator) {
			int either = edge.either(), other = edge.other(either);
			if (connected.sameComponent(either, other)) {
				return true;
			}
			connected.union(either, other);
		}
		return false;
	}
}