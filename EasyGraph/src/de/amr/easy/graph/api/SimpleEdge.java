package de.amr.easy.graph.api;

/**
 * Simple edge implementation.
 * 
 * @author Armin Reichert
 * 
 * @param <E>
 *          edge label type
 */
public class SimpleEdge<E> implements Edge<E> {

	protected final int u;
	protected final int v;
	protected E label;

	public SimpleEdge(int u, int v, E label) {
		this.u = u;
		this.v = v;
		this.label = label;
	}

	public SimpleEdge(int u, int v) {
		this.u = u;
		this.v = v;
	}

	@Override
	public int either() {
		return u;
	}

	@Override
	public int other() {
		return v;
	}

	@Override
	public E getLabel() {
		return label;
	}

	public void setLabel(E label) {
		this.label = label;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SimpleEdge<?> other = (SimpleEdge<?>) obj;
		if (u != other.u)
			return false;
		if (v != other.v)
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + u;
		result = prime * result + v;
		return result;
	}

	@Override
	public String toString() {
		return "(" + u + "," + v + ")";
	}

}