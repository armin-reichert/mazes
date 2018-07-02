package de.amr.easy.graph.api;

/**
 * An edge with comparable edge label. Can be used for example in priority queues.
 * 
 * @author Armin Reichert
 *
 * @param <E>
 *          comparable edge label type
 */
public class WeightedEdge<E extends Comparable<E>> extends SimpleEdge<E> implements Comparable<WeightedEdge<E>> {

	public WeightedEdge(int u, int v, E e) {
		super(u, v, e);
	}

	@Override
	public int compareTo(WeightedEdge<E> other) {
		if (label == null) {
			return other.label == null ? 0 : 1;
		}
		return label.compareTo(other.label);
	}
}