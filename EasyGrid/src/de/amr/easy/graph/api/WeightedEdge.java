package de.amr.easy.graph.api;

/**
 * Edge with weight of some comparable type.
 * 
 * @author Armin Reichert
 * 
 * @param <W>
 *          weight type (int, double, ...)
 */
public class WeightedEdge<W extends Comparable<W>> extends SimpleEdge implements Comparable<WeightedEdge<W>> {

	private W weight;

	public WeightedEdge(int u, int v, W weight) {
		super(u, v);
		this.weight = weight;
	}

	public WeightedEdge(int u, int v) {
		this(u, v, null);
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
	public int compareTo(WeightedEdge<W> other) {
		return weight.compareTo(other.weight);
	}

	@Override
	public String toString() {
		return String.format("[%4d,%4d,%10g]", u, v, weight);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((weight == null) ? 0 : weight.hashCode());
		return result;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		WeightedEdge<W> other = (WeightedEdge<W>) obj;
		if (weight == null) {
			if (other.weight != null)
				return false;
		} else if (!weight.equals(other.weight))
			return false;
		return true;
	}
}