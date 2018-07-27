package de.amr.easy.graph.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

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
	private Function<Integer, V> fnDefaultLabel;

	public VertexLabelsMap(Function<Integer, V> defaultLabel) {
		this.fnDefaultLabel = defaultLabel;
	}

	@Override
	public V get(int v) {
		return labels.containsKey(v) ? labels.get(v) : fnDefaultLabel.apply(v);
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
	public V getDefaultVertexLabel(int v) {
		return fnDefaultLabel.apply(v);
	}

	@Override
	public void setDefaultVertexLabel(Function<Integer, V> fnDefaultLabel) {
		this.fnDefaultLabel = fnDefaultLabel;
	}
}