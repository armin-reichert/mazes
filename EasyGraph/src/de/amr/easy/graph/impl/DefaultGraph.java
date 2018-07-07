package de.amr.easy.graph.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import de.amr.easy.graph.api.Edge;
import de.amr.easy.graph.api.Graph;
import de.amr.easy.graph.api.VertexLabels;

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
public class DefaultGraph<V, E> implements Graph<V, E> {

	protected final VertexLabels<V> vertexLabels = new VertexLabelsMap<>(null);
	protected final BiFunction<Integer, Integer, Edge> fnEdgeFactory;
	protected final Set<Integer> vertexSet = new HashSet<>();
	protected final Map<Integer, Set<Edge>> adjEdges = new HashMap<>();
	protected int numEdges; // number of undirected edges

	public DefaultGraph(BiFunction<Integer, Integer, Edge> fnEdgeFactory) {
		this.fnEdgeFactory = fnEdgeFactory;
	}

	@Override
	public V get(int v) {
		return vertexLabels.get(v);
	}

	@Override
	public void set(int v, V vertex) {
		if (!vertexSet.contains(v)) {
			throw new IllegalStateException();
		}
		vertexLabels.set(v, vertex);
	}

	@Override
	public void clearVertexLabels() {
		vertexLabels.clearVertexLabels();
	}

	@Override
	public void setDefaultVertexLabel(V vertex) {
		vertexLabels.setDefaultVertexLabel(vertex);
	}

	@Override
	public V getDefaultVertexLabel() {
		return vertexLabels.getDefaultVertexLabel();
	}

	@Override
	public void clearEdgeLabels() {
		edges().forEach(edge -> {

		});
	}

	@Override
	public E getEdgeLabel(int u, int v) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setEdgeLabel(int u, int v, E edge) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setDefaultEdgeLabel(BiFunction<Integer, Integer, E> fnDefaultLabel) {
		// TODO Auto-generated method stub

	}

	@Override
	public E getDefaultEdgeLabel(int u, int v) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addVertex(int v) {
		vertexSet.add(v);
		adjEdges.put(v, new HashSet<>());
	}

	@Override
	public void removeVertex(int v) {
		if (!vertexSet.contains(v)) {
			throw new IllegalStateException();
		}
		vertexSet.remove(v);
		adjEdges.remove(v);
	}

	@Override
	public void addEdge(int v, int w, E e) {
		assertVertexExists(v);
		assertVertexExists(w);
		Edge edge = fnEdgeFactory.apply(v, w);
		adjEdges.get(v).add(edge);
		adjEdges.get(w).add(edge);
		numEdges += 1;
	}

	@Override
	public void addEdge(int v, int w) {
		assertVertexExists(v);
		assertVertexExists(w);
		Edge edge = fnEdgeFactory.apply(v, w);
		adjEdges.get(v).add(edge);
		adjEdges.get(w).add(edge);
		numEdges += 1;
	}

	@Override
	public Optional<Edge> edge(int v, int w) {
		assertVertexExists(v);
		assertVertexExists(w);
		for (Edge edge : adjEdges.get(v)) {
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
		for (Edge edge : adjEdges.get(v)) {
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
	public int numVertices() {
		return vertexSet.size();
	}

	private Set<Edge> createEdgeSet() {
		Set<Edge> edges = new HashSet<>();
		for (int v : vertexSet) {
			for (Edge edge : adjEdges.get(v)) {
				edges.add(edge);
			}
		}
		return Collections.unmodifiableSet(edges);
	}

	@Override
	public Stream<Edge> edges() {
		return createEdgeSet().stream(); // TODO more efficient way possible?
	}

	@Override
	public int numEdges() {
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
		for (Edge e : adjEdges.get(v)) {
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
		s.append(numVertices()).append("\n");
		s.append(numEdges()).append("\n");
		for (int v : vertexSet) {
			s.append(v).append("\n");
		}
		for (Edge e : createEdgeSet()) {
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