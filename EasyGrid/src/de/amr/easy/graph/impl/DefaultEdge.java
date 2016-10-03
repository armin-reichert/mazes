package de.amr.easy.graph.impl;

import java.util.Objects;

import de.amr.easy.graph.api.Edge;

/**
 * Edge of a undirected graph.
 * 
 * @author Armin Reichert
 * 
 * @param <V>
 *          graph vertex type
 */
public class DefaultEdge<V> implements Edge<V> {

	protected V u;
	protected V v;

	public DefaultEdge(V u, V v) {
		Objects.requireNonNull(u);
		Objects.requireNonNull(v);
		this.u = u;
		this.v = v;
	}

	public void setEither(V either) {
		Objects.requireNonNull(either);
		this.u = either;
	}

	public void setOther(V other) {
		Objects.requireNonNull(other);
		this.v = other;
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
	public String toString() {
		return "{" + u + "," + v + "}";
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
		DefaultEdge<?> other = (DefaultEdge<?>) obj;
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
}