package de.amr.easy.graph.api;

/**
 * An edge with comparable edge label. Can be used for example in priority queues.
 * 
 * @author Armin Reichert
 *
 * @param <W>
 *          edge weight type
 */
public class WeightedEdge<W extends Comparable<W>> extends UndirectedEdge implements Comparable<WeightedEdge<W>> {

	private final W weight;

	public WeightedEdge(int u, int v, W weight) {
		super(u, v);
		this.weight = weight;
	}

	public W getWeight() {
		return weight;
	}

	@Override
	public int compareTo(WeightedEdge<W> other) {
		if (weight == null) {
			return other.weight == null ? 0 : 1;
		}
		return weight.compareTo(other.weight);
	}
}