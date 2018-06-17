package de.amr.easy.graph.api;

import java.util.Optional;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Undirected Graph.
 * <p>
 * Vertices are stored as indices. Vertex objects may be introduced by implementing classes.
 * 
 * @author Armin Reichert
 * 
 * @param <E>
 *          edge type
 */
public interface Graph<E extends Edge> {

	/**
	 * @return stream of the vertex indices of this graph
	 */
	IntStream vertices();

	/**
	 * @return the number of vertices of this graph
	 */
	default int numVertices() {
		return (int) vertices().count();
	}

	/**
	 * @return stream of the edges of this graph
	 */
	Stream<E> edges();

	/**
	 * @return the number of edges of this graph
	 */
	default int numEdges() {
		return (int) edges().count();
	}

	/**
	 * Adds the given vertex (index) to this graph.
	 * 
	 * @param v
	 *          a vertex index
	 */
	void addVertex(int v);

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
	IntStream adj(int v);

	/**
	 * Tells if the given vertices are connected by an edge.
	 * 
	 * @param v
	 *          some vertex
	 * @param w
	 *          some vertex
	 * @return {@code true} if there exists an edge between the vertices
	 */
	boolean hasEdge(int v, int w);

	/**
	 * @param v
	 *          a vertex
	 * @return the number of vertices adjacent to <code>v</code>
	 */
	int degree(int v);
}
