package de.amr.easy.graph.api;

/**
 * Edge with label ("weight") of some comparable type.
 * 
 * @author Armin Reichert
 * 
 * @param <W>
 *          weight type (int, double, ...)
 */
public class WeightedEdge<W extends Comparable<W>> extends SimpleEdge<W> implements Comparable<WeightedEdge<W>> {

	public WeightedEdge(int u, int v, W w) {
		super(u, v, w);
	}

	public WeightedEdge(int u, int v) {
		this(u, v, null);
	}

	@Override
	public int compareTo(WeightedEdge<W> other) {
		return label.compareTo(other.label);
	}

	@Override
	public String toString() {
		return String.format("[%d,%d,%s]", u, v, String.valueOf(label));
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((label == null) ? 0 : label.hashCode());
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
		if (label == null) {
			if (other.label != null)
				return false;
		} else if (!label.equals(other.label))
			return false;
		return true;
	}
}