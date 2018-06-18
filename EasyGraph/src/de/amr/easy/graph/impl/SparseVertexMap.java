package de.amr.easy.graph.impl;

import java.util.HashMap;
import java.util.Map;

import de.amr.easy.graph.api.VertexMap;

/**
 * Vertex map implementation for sparse graphs.
 * 
 * @author Armin Reichert
 *
 * @param <V>
 *          vertex type
 */
public class SparseVertexMap<V> implements VertexMap<V> {

	private final Map<Integer, V> map = new HashMap<>();
	private V defaultVertex;

	@Override
	public V get(int v) {
		return map.containsKey(v) ? map.get(v) : defaultVertex;
	}

	@Override
	public void set(int v, V vertex) {
		map.put(v, vertex);
	}

	@Override
	public void clear() {
		map.clear();
	}

	@Override
	public V getDefaultVertex() {
		return defaultVertex;
	}

	@Override
	public void setDefaultVertex(V vertex) {
		defaultVertex = vertex;
	}
}