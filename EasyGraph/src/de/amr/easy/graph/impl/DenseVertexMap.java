package de.amr.easy.graph.impl;

import de.amr.easy.graph.api.VertexMap;

/**
 * Vertex map implementation for dense graphs.
 * 
 * @author Armin Reichert
 *
 * @param <V>
 *          vertex type
 */
public class DenseVertexMap<V> implements VertexMap<V> {

	private Object[] map;
	private V defaultVertex;

	public DenseVertexMap(int size) {
		map = new Object[size];
	}

	@SuppressWarnings("unchecked")
	@Override
	public V get(int v) {
		return (V) (map[v] != null ? map[v] : defaultVertex);
	}

	@Override
	public void set(int v, V vertex) {
		map[v] = vertex;
	}

	@Override
	public void clear() {
		map = new Object[map.length];
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