package de.amr.easy.graph.alg;

import java.util.function.Predicate;

import de.amr.easy.data.Partition;
import de.amr.easy.graph.api.Edge;
import de.amr.easy.graph.api.Graph;

public class CycleChecker<V, E extends Edge<V>> implements Predicate<Graph<V, E>> {

	@Override
	public boolean test(Graph<V, E> ugraph) {
		Partition<V> connected = new Partition<>();
		for (E edge : (Iterable<E>) ugraph.edgeStream()::iterator) {
			V either = edge.either(), other = edge.other(either);
			Partition.EquivClass eitherComp = connected.find(either), otherComp = connected.find(other);
			if (eitherComp == otherComp) {
				return true;
			}
			connected.union(eitherComp, otherComp);
		}
		return false;
	}
}