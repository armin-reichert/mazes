package de.amr.easy.graph.api;

import java.util.Optional;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Undirected Graph.
 * 
 * @author Armin Reichert
 * 
 * @param <E>
 *          edge type
 */
public interface Graph<E extends Edge> {

	/**
	 * @return a stream of the vertices of this graph
	 */
	IntStream vertices();

	/**
	 * @return the number of vertices of this graph
	 */
	int vertexCount();

	/**
	 * @return a stream of the edges of this graph
	 */
	Stream<E> edges();

	/**
	 * @return the number of edges of this graph
	 */
	int edgeCount();

	/**
	 * Adds the given vertex to this graph.
	 * 
	 * @param vertex
	 *          a vertex
	 */
	void addVertex(int vertex);

	/**
	 * Connects the given vertices.
	 * 
	 * @param v
	 *          a vertex
	 * @param w
	 *          a vertex
	 */
	void addEdge(int v, int w);

	/**
	 * @param v
	 *          a vertex
	 * @param w
	 *          a vertex
	 * @return the edge between the vertices if it exists
	 */
	Optional<E> edge(int v, int w);

	/**
	 * Removes the edge between the given vertices from this graph.
	 * 
	 * @param edge
	 *          an edge
	 */
	void removeEdge(int v, int w);

	/**
	 * Removes the given edge.
	 * 
	 * @param edge
	 *          an edge
	 */
	default void removeEdge(E edge) {
		removeEdge(edge.either(), edge.other());
	}

	/**
	 * Removes all edges from this graph.
	 */
	void removeEdges();

	/**
	 * @param v
	 *          a vertex
	 * @return all vertices adjacent to the given vertex
	 */
	IntStream adjVertices(int v);

	/**
	 * Tells if the given vertices are "adjacent" that is connected by an edge.
	 * 
	 * @param v
	 *          a vertex
	 * @param w
	 *          a vertex
	 * @return <code>true</code> if there exists an edge between the vertices
	 */
	boolean adjacent(int v, int w);

	/**
	 * @param v
	 *          a vertex
	 * @return the number of vertices adjacent to <code>v</code>
	 */
	int degree(int v);
}
