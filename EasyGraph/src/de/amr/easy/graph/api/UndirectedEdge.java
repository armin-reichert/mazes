package de.amr.easy.graph.api;

import de.amr.easy.data.TwoSet;

/**
 * Undirected edge implementation.
 * 
 * @author Armin Reichert
 */
public class UndirectedEdge implements Edge {

	private final TwoSet<Integer> vertices;

	public UndirectedEdge(int u, int v) {
		vertices = TwoSet.of(u, v);
	}

	@Override
	public int either() {
		return vertices.e1;
	}

	@Override
	public int other() {
		return vertices.e2;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof UndirectedEdge)) {
			return false;
		}
		UndirectedEdge other = (UndirectedEdge) obj;
		return vertices.equals(other.vertices);
	}

	@Override
	public int hashCode() {
		return vertices.hashCode();
	}
}