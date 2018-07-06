package de.amr.easy.graph.api;

/**
 * An edge with comparable edge label. Can be used for example in priority queues.
 * 
 * @author Armin Reichert
 *
 * @param <E>
 *          edge weight type
 */
public class WeightedEdge<E extends Comparable<E>> extends SimpleEdge implements Comparable<WeightedEdge<E>> {

	private final E weight;

	public WeightedEdge(int u, int v, E label) {
		super(u, v);
		this.weight = label;
	}

	public E getWeight() {
		return weight;
	}

	@Override
	public int compareTo(WeightedEdge<E> other) {
		if (weight == null) {
			return other.weight == null ? 0 : 1;
		}
		return weight.compareTo(other.weight);
	}
}