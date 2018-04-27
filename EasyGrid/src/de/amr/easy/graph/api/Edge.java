package de.amr.easy.graph.api;

/**
 * An undirected graph edge.
 *
 */
public interface Edge {

	/**
	 * @return one vertex of this edge
	 */
	public int either();

	/**
	 * @param v
	 *          one vertex of this edge
	 * @return the other vertex of this edge
	 */
	public int other(int either);
}