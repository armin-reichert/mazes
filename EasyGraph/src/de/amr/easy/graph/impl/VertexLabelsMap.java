package de.amr.easy.graph.impl;

import java.util.HashMap;
import java.util.Map;

import de.amr.easy.graph.api.VertexLabels;

/**
 * Vertex labels implementation for sparse graphs.
 * 
 * @author Armin Reichert
 *
 * @param <V>
 *          vertex label type
 */
public class VertexLabelsMap<V> implements VertexLabels<V> {

	private final Map<Integer, V> labels = new HashMap<>();
	private V defaultLabel;

	public VertexLabelsMap(V defaultLabel) {
		this.defaultLabel = defaultLabel;
	}

	@Override
	public V get(int v) {
		return labels.containsKey(v) ? labels.get(v) : defaultLabel;
	}

	@Override
	public void set(int v, V vertexLabel) {
		labels.put(v, vertexLabel);
	}

	@Override
	public void clearVertexLabels() {
		labels.clear();
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