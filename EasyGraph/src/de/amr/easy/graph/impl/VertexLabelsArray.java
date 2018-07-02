package de.amr.easy.graph.impl;

import de.amr.easy.graph.api.VertexLabels;

/**
 * Vertex labels implementation for dense graphs.
 * 
 * @author Armin Reichert
 *
 * @param <V>
 *          vertex label type
 */
public class VertexLabelsArray<V> implements VertexLabels<V> {

	private Object[] labels;
	private V defaultLabel;

	public VertexLabelsArray(int size) {
		labels = new Object[size];
	}

	@SuppressWarnings("unchecked")
	@Override
	public V get(int v) {
		return (V) (labels[v] != null ? labels[v] : defaultLabel);
	}

	@Override
	public void set(int v, V vertex) {
		labels[v] = vertex;
	}

	@Override
	public void clearVertexLabels() {
		labels = new Object[labels.length];
	}

	@Override
	public V getDefaultVertexLabel() {
		return defaultLabel;
	}

	@Override
	public void setDefaultVertexLabel(V label) {
		defaultLabel = label;
	}
}