package de.amr.easy.graph.api;

/**
 * An undirected graph edge.
 * 
 * @param <E>
 *          edge label type
 */
public interface Edge<E> {

	/**
	 * @return one vertex of this edge
	 */
	int either();

	/**
	 * @return the other vertex of this edge
	 */
	int other();

	/**
	 * @return the edge label
	 */
	E getLabel();
}