package de.amr.easy.graph.api;

import java.util.Optional;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Interface for a graph with vertex and edge labels. Vertices are integers which may be labeled by
 * some arbitrary label objects of a given type. Edges can also be labeled by objects an arbitrary
 * type.
 * 
 * @author Armin Reichert
 *
 * @param <V>
 *          vertex label type
 * @param <E>
 *          edge label type
 */
public interface Graph<V, E> extends VertexLabels<V>, EdgeLabels<E> {

	/**
	 * @return stream of the vertices of this graph
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
	Stream<Edge> edges();

	/**
	 * @return the number of edges of this graph
	 */
	default int numEdges() {
		return (int) edges().count();
	}

	/**
	 * Adds the given vertex to this graph.
	 * 
	 * @param v
	 *          a vertex
	 */
	void addVertex(int v);

	/**
	 * Removes the given vertex from this graph.
	 * 
	 * @param v
	 *          a vertex
	 */
	void removeVertex(int v);

	/**
	 * @param v
	 *          a vertex
	 * @return all "adjacent" vertices (connected by some edge) to the given vertex
	 */
	IntStream adj(int v);

	/**
	 * Tells if the given vertices are connected by some edge.
	 * 
	 * @param v
	 *          a vertex
	 * @param w
	 *          a vertex
	 * @return {@code true} if there exists an edge between the vertices
	 */
	boolean adjacent(int v, int w);

	/**
	 * @param v
	 *          a vertex
	 * @return the number of vertices adjacent to <code>v</code>
	 */
	int degree(int v);

	/**
	 * Adds an edge between the given vertices.
	 * 
	 * @param v
	 *          a vertex
	 * @param w
	 *          a vertex
	 */
	void addEdge(int v, int w);

	/**
	 * Adds an edge wit a label between the given vertices.
	 * 
	 * @param v
	 *          a vertex
	 * @param w
	 *          a vertex
	 * @param e
	 *          edge label
	 */
	void addEdge(int v, int w, E e);

	/**
	 * @param v
	 *          a vertex
	 * @param w
	 *          a vertex
	 * @return the edge between the vertices if it exists
	 */
	Optional<Edge> edge(int v, int w);

	/**
	 * Removes the edge between the given vertices.
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
	default void removeEdge(Edge edge) {
		removeEdge(edge.either(), edge.other());
	}

	/**
	 * Removes all edges from this graph.
	 */
	void removeEdges();
}
