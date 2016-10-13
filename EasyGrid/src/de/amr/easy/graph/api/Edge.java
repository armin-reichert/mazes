package de.amr.easy.graph.api;

/**
 * An undirected graph edge.
 *
 * @param <V>
 *          the vertex type
 */
public interface Edge<V> extends Comparable<Edge<V>> {

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

	/**
	 * 
	 * @return the weight of this edge which is 0 be default.
	 */
	public double getWeight();

	/**
	 * Sets a weight for this edge.
	 * 
	 * @param weight
	 *          some number
	 */
	public void setWeight(double weight);

}
