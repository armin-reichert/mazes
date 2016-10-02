package de.amr.easy.graph.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

import de.amr.easy.graph.api.Edge;
import de.amr.easy.graph.api.Graph;

/**
 * Adjacency set based implementation of an undirected graph.
 * 
 * @author Armin Reichert
 * 
 * @param <V>
 *          vertex type
 * @param <E>
 *          edge type
 */
public class DefaultGraph<V, E extends Edge<V>> implements Graph<V, E> {

	private final Set<V> vertexSet = new HashSet<>();
	private final Map<V, Set<E>> adjEdges = new HashMap<>();
	private int numEdges; // number of undirected edges

	public DefaultGraph() {
	}

	@Override
	public void addVertex(V vertex) {
		vertexSet.add(vertex);
		adjEdges.put(vertex, new HashSet<E>()); // TODO improve
	}

	@Override
	public void addEdge(E edge) {
		V v = edge.either();
		V w = edge.other(v);
		assertVertexExists(v);
		assertVertexExists(w);
		adjEdges.get(v).add(edge);
		adjEdges.get(w).add(edge);
		numEdges += 1;
	}

	@Override
	public E edge(V v, V w) {
		assertVertexExists(v);
		assertVertexExists(w);
		for (E edge : adjEdges.get(v)) {
			if (w == edge.either() || w == edge.other(v)) {
				return edge;
			}
		}
		return null;
	}

	@Override
	public void removeEdge(E edge) {
		V v = edge.either();
		V w = edge.other(v);
		assertVertexExists(v);
		assertVertexExists(w);
		adjEdges.get(v).remove(edge);
		adjEdges.get(w).remove(edge);
		numEdges -= 1;
	}

	@Override
	public void removeEdges() {
		for (V v : vertexSet) {
			adjEdges.get(v).clear();
		}
		numEdges = 0;
	}

	@Override
	public boolean adjacent(V v, V w) {
		assertVertexExists(v);
		assertVertexExists(w);
		for (E edge : adjEdges.get(v)) {
			if (w == edge.either() || w == edge.other(v)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public Set<V> vertexSequence() {
		return Collections.unmodifiableSet((vertexSet));
	}

	@Override
	public Stream<V> vertexStream() {
		return vertexSet.stream();
	}

	@Override
	public int vertexCount() {
		return vertexSet.size();
	}

	@Override
	public Set<E> edgeSequence() {
		Set<E> edges = new HashSet<>();
		for (V v : vertexSet) {
			for (E edge : adjEdges.get(v)) {
				edges.add(edge);
			}
		}
		return Collections.unmodifiableSet(edges);
	}

	@Override
	public Stream<E> edgeStream() {
		return edgeSequence().stream(); // TODO more efficient way possible?
	}

	@Override
	public int edgeCount() {
		return numEdges;
	}

	@Override
	public int degree(V v) {
		assertVertexExists(v);
		return adjEdges.get(v).size();
	}

	@Override
	public Iterable<V> adjVertices(V v) {
		assertVertexExists(v);
		List<V> result = new ArrayList<>();
		for (E e : adjEdges.get(v)) {
			if (e.either() == v) {
				result.add(e.other(v));
			} else {
				result.add(e.either());
			}
		}
		return result;
	}

	@Override
	public String toString() {
		StringBuilder s = new StringBuilder();
		s.append(vertexCount()).append("\n");
		s.append(edgeCount()).append("\n");
		for (V v : vertexSet) {
			s.append(v).append("\n");
		}
		for (E e : edgeSequence()) {
			V v = e.either();
			V w = e.other(v);
			s.append(v).append(" ").append(w).append("\n");
		}
		return s.toString();
	}

	protected void assertVertexExists(V v) {
		if (!vertexSet.contains(v)) {
			throw new IllegalStateException("Vertex not in graph: " + v);
		}
	}
}
