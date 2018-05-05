package de.amr.easy.graph.api;

/**
 * An undirected graph edge.
 */
public interface Edge {

	/**
	 * @return one vertex of this edge
	 */
	public int either();

	/**
	 * @param either
	 *          one vertex of this edge
	 * @return the vertex opposite to the given vertex
	 */
	public int other(int either);
}