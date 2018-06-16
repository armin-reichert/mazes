package de.amr.easy.graph.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import de.amr.easy.graph.api.Graph;
import de.amr.easy.graph.api.SimpleEdge;

/**
 * Adjacency set based implementation of an undirected graph.
 * 
 * @author Armin Reichert
 * 
 * @param <E>
 *          edge type
 */
public class DefaultGraph implements Graph<SimpleEdge> {

	private final Set<Integer> vertexSet = new HashSet<>();
	private final Map<Integer, Set<SimpleEdge>> adjEdges = new HashMap<>();
	private int numEdges; // number of undirected edges

	public DefaultGraph() {
	}

	@Override
	public void addVertex(int vertex) {
		vertexSet.add(vertex);
		adjEdges.put(vertex, new HashSet<SimpleEdge>());
	}

	@Override
	public void addEdge(int v, int w) {
		assertVertexExists(v);
		assertVertexExists(w);
		SimpleEdge edge = new SimpleEdge(v, w);
		adjEdges.get(v).add(edge);
		adjEdges.get(w).add(edge);
		numEdges += 1;
	}

	@Override
	public Optional<SimpleEdge> edge(int v, int w) {
		assertVertexExists(v);
		assertVertexExists(w);
		for (SimpleEdge edge : adjEdges.get(v)) {
			if (w == edge.either() || w == edge.other()) {
				return Optional.of(edge);
			}
		}
		return Optional.empty();
	}

	@Override
	public void removeEdge(int v, int w) {
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
		for (int v : vertexSet) {
			adjEdges.get(v).clear();
		}
		numEdges = 0;
	}

	@Override
	public boolean adjacent(int v, int w) {
		assertVertexExists(v);
		assertVertexExists(w);
		for (SimpleEdge edge : adjEdges.get(v)) {
			if (w == edge.either() || w == edge.other()) {
				return true;
			}
		}
		return false;
	}

	@Override
	public IntStream vertices() {
		return vertexSet.stream().mapToInt(Integer::intValue);
	}

	@Override
	public int vertexCount() {
		return vertexSet.size();
	}

	private Set<SimpleEdge> createEdgeSet() {
		Set<SimpleEdge> edges = new HashSet<>();
		for (int v : vertexSet) {
			for (SimpleEdge edge : adjEdges.get(v)) {
				edges.add(edge);
			}
		}
		return Collections.unmodifiableSet(edges);
	}

	@Override
	public Stream<SimpleEdge> edges() {
		return createEdgeSet().stream(); // TODO more efficient way possible?
	}

	@Override
	public int edgeCount() {
		return numEdges;
	}

	@Override
	public int degree(int v) {
		assertVertexExists(v);
		return adjEdges.get(v).size();
	}

	@Override
	public IntStream adj(int v) {
		assertVertexExists(v);
		List<Integer> result = new ArrayList<>();
		for (SimpleEdge e : adjEdges.get(v)) {
			if (e.either() == v) {
				result.add(e.other());
			} else {
				result.add(e.either());
			}
		}
		return result.stream().mapToInt(Integer::intValue);
	}

	@Override
	public String toString() {
		StringBuilder s = new StringBuilder();
		s.append(vertexCount()).append("\n");
		s.append(edgeCount()).append("\n");
		for (int v : vertexSet) {
			s.append(v).append("\n");
		}
		for (SimpleEdge e : createEdgeSet()) {
			s.append(e.either()).append(" ").append(e.other()).append("\n");
		}
		return s.toString();
	}

	protected void assertVertexExists(int v) {
		if (!vertexSet.contains(v)) {
			throw new IllegalStateException("Vertex not in graph: " + v);
		}
	}
}
