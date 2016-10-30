package de.amr.easy.graph.api;

/**
 * Edge with weight of some comparable type.
 * 
 * @author Armin Reichert
 * 
 * @param <V>
 *          graph vertex type
 * @param <W>
 *          weight type (int, double, ...)
 */
public class WeightedEdge<V, W extends Comparable<W>> extends SimpleEdge<V>
		implements Edge<V>, Comparable<WeightedEdge<V, W>> {

	private W weight;

	public WeightedEdge(V u, V v, W weight) {
		super(u, v);
		this.weight = weight;
	}

	public WeightedEdge(V head, V tail) {
		this(head, tail, null);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((u == null) ? 0 : u.hashCode());
		result = prime * result + ((v == null) ? 0 : v.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		WeightedEdge<?, ?> other = (WeightedEdge<?, ?>) obj;
		if (u == null) {
			if (other.u != null)
				return false;
		} else if (!u.equals(other.u))
			return false;
		if (v == null) {
			if (other.v != null)
				return false;
		} else if (!v.equals(other.v))
			return false;
		return true;
	}

	/**
	 * @return the weight of this edge
	 */
	public W getWeight() {
		return weight;
	}

	/**
	 * Sets the weight of this edge.
	 * 
	 * @param weight
	 *          the weight
	 */
	public void setWeight(W weight) {
		this.weight = weight;
	}

	@Override
	public int compareTo(WeightedEdge<V, W> other) {
		return weight.compareTo(other.weight);
	}

	@Override
	public String toString() {
		return String.format("[%4d,%4d,%10g]", u, v, weight);
	}
}