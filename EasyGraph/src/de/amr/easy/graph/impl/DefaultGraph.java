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
import de.amr.easy.graph.api.VertexMap;

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
public class DefaultGraph<V, E extends Edge> implements Graph<V, E>, VertexMap<V> {

	protected final VertexMap<V> vertexMap = new SparseVertexMap<>();
	protected final BiFunction<Integer, Integer, E> fnEdgeFactory;
	protected final Set<Integer> vertexSet = new HashSet<>();
	protected final Map<Integer, Set<E>> adjEdges = new HashMap<>();
	protected int numEdges; // number of undirected edges

	public DefaultGraph(BiFunction<Integer, Integer, E> fnEdgeFactory) {
		this.fnEdgeFactory = fnEdgeFactory;
	}

	@Override
	public V get(int v) {
		return vertexMap.get(v);
	}

	@Override
	public void set(int v, V vertex) {
		if (!vertexSet.contains(v)) {
			throw new IllegalStateException();
		}
		vertexMap.set(v, vertex);
	}

	@Override
	public void clear() {
		vertexMap.clear();
	}

	@Override
	public void setDefaultVertex(V vertex) {
		vertexMap.setDefaultVertex(vertex);
	}

	@Override
	public V getDefaultVertex() {
		return vertexMap.getDefaultVertex();
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
	public void addEdge(int v, int w) {
		assertVertexExists(v);
		assertVertexExists(w);
		E edge = fnEdgeFactory.apply(v, w);
		adjEdges.get(v).add(edge);
		adjEdges.get(w).add(edge);
		numEdges += 1;
	}

	@Override
	public Optional<E> edge(int v, int w) {
		assertVertexExists(v);
		assertVertexExists(w);
		for (E edge : adjEdges.get(v)) {
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
	public boolean hasEdge(int v, int w) {
		assertVertexExists(v);
		assertVertexExists(w);
		for (E edge : adjEdges.get(v)) {
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

	private Set<E> createEdgeSet() {
		Set<E> edges = new HashSet<>();
		for (int v : vertexSet) {
			for (E edge : adjEdges.get(v)) {
				edges.add(edge);
			}
		}
		return Collections.unmodifiableSet(edges);
	}

	@Override
	public Stream<E> edges() {
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
		for (E e : adjEdges.get(v)) {
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
		for (E e : createEdgeSet()) {
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
