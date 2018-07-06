package de.amr.easy.graph.api;

/**
 * An undirected graph edge.
 */
public interface Edge {

	/**
	 * @return one vertex of this edge
	 */
	int either();

	/**
	 * @return the other vertex of this edge
	 */
	int other();
}