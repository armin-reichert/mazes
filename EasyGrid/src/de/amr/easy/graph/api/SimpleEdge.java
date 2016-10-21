package de.amr.easy.graph.api;

import java.util.Objects;

/**
 * Simple edge.
 * 
 * @author Armin Reichert
 *
 * @param <V>
 *          vertex type
 */
public class SimpleEdge<V> implements Edge<V> {

	protected final V u;
	protected final V v;

	public SimpleEdge(V u, V v) {
		Objects.nonNull(u);
		Objects.nonNull(v);
		this.u = u;
		this.v = v;
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
}