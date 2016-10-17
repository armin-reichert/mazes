package de.amr.easy.graph.api;

import java.util.Objects;

/**
 * Edge of a weighted graph. Edge weight type is <code>double</code>.
 * 
 * @author Armin Reichert
 * 
 * @param <V>
 *          graph vertex type
 */
public class WeightedEdge<V> implements Edge<V> {

	private final V u;
	private final V v;
	private double weight;

	public WeightedEdge(V u, V v, double weight) {
		Objects.requireNonNull(u);
		Objects.requireNonNull(v);
		this.u = u;
		this.v = v;
		this.weight = weight;
	}

	public WeightedEdge(V head, V tail) {
		this(head, tail, 0);
	}

	@Override
	public V either() {
		return u;
	}

	@Override
	public V other(V v) {
		if (v == this.v) {
			return this.u;
		}
		if (v == this.u) {
			return this.v;
		}
		throw new IllegalStateException();
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
		WeightedEdge<?> other = (WeightedEdge<?>) obj;
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

	@Override
	public double getWeight() {
		return weight;
	}

	@Override
	public void setWeight(double weight) {
		this.weight = weight;
	}

	@Override
	public int compareTo(Edge<V> edge) {
		if (weight < edge.getWeight()) {
			return -1;
		} else if (weight > edge.getWeight()) {
			return 1;
		} else {
			return 0;
		}
	}

	@Override
	public String toString() {
		return "{" + u + "," + v + "}:" + weight;
	}
}