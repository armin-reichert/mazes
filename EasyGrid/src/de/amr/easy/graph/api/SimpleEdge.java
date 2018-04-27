package de.amr.easy.graph.api;

/**
 * Simple edge implementation.
 * 
 * @author Armin Reichert
 *
 */
public class SimpleEdge implements Edge {

	protected final int u;
	protected final int v;

	public SimpleEdge(int u, int v) {
		this.u = u;
		this.v = v;
	}

	@Override
	public int either() {
		return u;
	}

	@Override
	public int other(int x) {
		if (x != u && x != v) {
			throw new IllegalStateException();
		}
		return x == u ? v : u;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SimpleEdge other = (SimpleEdge) obj;
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

}