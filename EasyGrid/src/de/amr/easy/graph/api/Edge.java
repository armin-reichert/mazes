package de.amr.easy.graph.api;

/**
 * An undirected graph edge.
 *
 * @param <V>
 *          the vertex type
 */
public interface Edge<V> {

	/**
	 * @return one vertex of this edge
	 */
	public V either();

	/**
	 * @param v
	 *          one vertex of this edge
	 * @return the other vertex of this edge
	 */
	public V other(V v);
}