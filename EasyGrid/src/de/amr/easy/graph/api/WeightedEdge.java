package de.amr.easy.graph.api;

/**
 * Weighted edge interface.
 * 
 * @author Armin Reichert
 * 
 * @param <V>
 *          vertex type
 */
public interface WeightedEdge<V> extends Edge<V>, Comparable<WeightedEdge<V>> {

	public double getWeight();

	public void setWeight(double weight);
}
