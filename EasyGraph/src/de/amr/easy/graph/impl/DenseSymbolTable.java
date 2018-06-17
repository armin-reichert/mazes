package de.amr.easy.graph.impl;

import de.amr.easy.graph.api.Vertex;

/**
 * Symbol table implementation for dense graphs.
 * 
 * @author Armin Reichert
 *
 * @param <V>
 *          cell content type
 */
public class DenseSymbolTable<V> implements Vertex<V> {

	private Object[] vertexTable;
	private V defaultVertex;

	public DenseSymbolTable(int size) {
		vertexTable = new Object[size];
	}

	@SuppressWarnings("unchecked")
	@Override
	public V get(int v) {
		V vertex = (V) vertexTable[v];
		return vertex != null ? vertex : (V) defaultVertex;
	}

	@Override
	public void set(int v, V vertex) {
		vertexTable[v] = vertex;
	}

	@Override
	public void clearVertexObjects() {
		vertexTable = new Object[vertexTable.length];
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
		return false;
	}
}