package de.amr.easy.graph.impl;

import java.util.HashMap;
import java.util.Map;

import de.amr.easy.graph.api.Vertex;

/**
 * Symbol table implementation for sparse graphs.
 * 
 * @author Armin Reichert
 *
 * @param <V>
 *          vertex type
 */
public class SparseSymbolTable<V> implements Vertex<V> {

	private final Map<Integer, V> vertexMap = new HashMap<>();
	private V defaultVertex;

	@Override
	public V get(int v) {
		V vertex = vertexMap.get(v);
		return vertex != null ? vertex : defaultVertex;
	}

	@Override
	public void set(int v, V vertex) {
		vertexMap.put(v, vertex);
	}

	@Override
	public void clearVertexObjects() {
		vertexMap.clear();
	}

	@Override
	public V getDefaultVertex() {
		return defaultVertex;
	}

	@Override
	public void setDefaultVertex(V vertex) {
		defaultVertex = vertex;
	}

	@Override
	public boolean isSparse() {
		return true;
	}
}