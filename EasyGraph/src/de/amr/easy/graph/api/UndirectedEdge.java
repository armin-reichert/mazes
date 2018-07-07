package de.amr.easy.graph.api;

/**
 * Undirected edge implementation. Two undirected edges are equal if their vertex sets are equal.
 * 
 * @author Armin Reichert
 */
public class UndirectedEdge implements Edge {

	private final int u;
	private final int v;

	public UndirectedEdge(int u, int v) {
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
	public boolean equals(Object obj) {
		if (obj instanceof UndirectedEdge) {
			UndirectedEdge other = (UndirectedEdge) obj;
			return u == other.u && v == other.v || u == other.v && v == other.u;
		}
		return false;
	}

	@Override
	public int hashCode() {
		return Integer.hashCode(u) + Integer.hashCode(v);
	}
}