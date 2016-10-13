package de.amr.easy.graph.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

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
public class DefaultGraph<V> implements Graph<V, DefaultEdge<V>> {

	private final Set<V> vertexSet = new HashSet<>();
	private final Map<V, Set<DefaultEdge<V>>> adjEdges = new HashMap<>();
	private int numEdges; // number of undirected edges

	public DefaultGraph() {
	}

	@Override
	public void addVertex(V vertex) {
		vertexSet.add(vertex);
		adjEdges.put(vertex, new HashSet<DefaultEdge<V>>()); // TODO improve
	}

	@Override
	public void addEdge(V v, V w) {
		assertVertexExists(v);
		assertVertexExists(w);
		DefaultEdge<V> edge = new DefaultEdge<>(v, w);
		adjEdges.get(v).add(edge);
		adjEdges.get(w).add(edge);
		numEdges += 1;
	}

	@Override
	public Optional<DefaultEdge<V>> edge(V v, V w) {
		assertVertexExists(v);
		assertVertexExists(w);
		for (DefaultEdge<V> edge : adjEdges.get(v)) {
			if (w == edge.either() || w == edge.other(v)) {
				return Optional.of(edge);
			}
		}
		return Optional.empty();
	}

	@Override
	public void removeEdge(V v, V w) {
		assertVertexExists(v);
		assertVertexExists(w);
		edge(v, w).ifPresent(edge -> {
			adjEdges.get(v).remove(edge);
			adjEdges.get(w).remove(edge);
			numEdges -= 1;

		});
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
		for (DefaultEdge<V> edge : adjEdges.get(v)) {
			if (w == edge.either() || w == edge.other(v)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public Stream<V> vertexStream() {
		return vertexSet.stream();
	}

	@Override
	public int vertexCount() {
		return vertexSet.size();
	}

	private Set<DefaultEdge<V>> createEdgeSet() {
		Set<DefaultEdge<V>> edges = new HashSet<>();
		for (V v : vertexSet) {
			for (DefaultEdge<V> edge : adjEdges.get(v)) {
				edges.add(edge);
			}
		}
		return Collections.unmodifiableSet(edges);
	}

	@Override
	public Stream<DefaultEdge<V>> edgeStream() {
		return createEdgeSet().stream(); // TODO more efficient way possible?
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
	public Stream<V> adjVertices(V v) {
		assertVertexExists(v);
		List<V> result = new ArrayList<>();
		for (DefaultEdge<V> e : adjEdges.get(v)) {
			if (e.either() == v) {
				result.add(e.other(v));
			} else {
				result.add(e.either());
			}
		}
		return result.stream();
	}

	@Override
	public String toString() {
		StringBuilder s = new StringBuilder();
		s.append(vertexCount()).append("\n");
		s.append(edgeCount()).append("\n");
		for (V v : vertexSet) {
			s.append(v).append("\n");
		}
		for (DefaultEdge<V> e : createEdgeSet()) {
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
