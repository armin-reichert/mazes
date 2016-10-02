package de.amr.easy.graph.impl;

import de.amr.easy.graph.api.WeightedEdge;

/**
 * Edge of a weighted graph. Edge weight type is <code>double</code>.
 * 
 * @author Armin Reichert
 * 
 * @param <V>
 *          graph vertex type
 */
public class DefaultWeightedEdge<V> extends DefaultEdge<V> implements WeightedEdge<V> {

	private double weight;

	public DefaultWeightedEdge(V head, V tail, double weight) {
		super(head, tail);
		this.weight = weight;
	}

	public DefaultWeightedEdge(V head, V tail) {
		this(head, tail, 0.0);
	}

	@Override
	public double getWeight() {
		return weight;
	}

	@Override
	public void setWeight(double weight) {
		this.weight = weight;
	}

	@Override
	public int compareTo(WeightedEdge<V> edge) {
		if (weight < edge.getWeight()) {
			return -1;
		} else if (weight > edge.getWeight()) {
			return 1;
		} else {
			return 0;
		}
	}

	@Override
	public String toString() {
		return "{" + u + "," + v + "}:" + weight;
	}
}