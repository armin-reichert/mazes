package de.amr.easy.graph.api;

/**
 * A graph edge. Vertices are represented as integers.
 */
public interface Edge {

	/**
	 * @return either end of this edge
	 */
	int either();

	/**
	 * @return other end of this edge
	 */
	int other();
}