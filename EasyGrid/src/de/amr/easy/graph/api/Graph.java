package de.amr.easy.graph.api;

import java.util.Optional;
import java.util.stream.Stream;

/**
 * Undirected Graph.
 * 
 * @author Armin Reichert
 * 
 * @param <V>
 *          vertex type
 * @param <E>
 *          edge type
 */
public interface Graph<V, E extends Edge<V>> {

	/**
	 * @return sequence of the vertices of this graph
	 */
	public Iterable<V> vertexSequence();

	/**
	 * @return stream of the vertices of this graph
	 */
	public Stream<V> vertexStream();

	/**
	 * @return the number of vertices of this graph
	 */
	public int vertexCount();

	/**
	 * @return sequence of the edges of this graph
	 */
//	public Iterable<E> edgeSequence();

	/**
	 * @return the edges of this graph as stream
	 */
	public Stream<E> edgeStream();

	/**
	 * @return the number of edges of this graph
	 */
	public int edgeCount();

	/**
	 * Adds the given vertex to this graph.
	 * 
	 * @param vertex
	 *          a vertex
	 */
	public void addVertex(V vertex);

	/**
	 * Adds the given edge to this graph.
	 * 
	 * @param edge
	 *          an edge
	 */
	public void addEdge(E edge);

	/**
	 * @param v
	 *          a vertex
	 * @param w
	 *          a vertex
	 * @return the edge between the vertices if it exists
	 */
	public Optional<E> edge(V v, V w);

	/**
	 * Removes the given edge from this graph.
	 * 
	 * @param edge
	 *          an edge
	 */
	public void removeEdge(E edge);

	/**
	 * Removes all edges from this graph.
	 */
	public void removeEdges();

	/**
	 * @param v
	 *          a vertex
	 * @return the vertices adjacent to the given vertex
	 */
	public Iterable<V> adjVertices(V v);

	/**
	 * Tells if the given vertices are adjacent.
	 * 
	 * @param v
	 *          a vertex
	 * @param w
	 *          a vertex
	 * @return <code>true</code> if there exists an edge between the vertices
	 */
	public boolean adjacent(V v, V w);

	/**
	 * @param v
	 *          a vertex
	 * @return the number of vertices adjacent to <code>v</code>
	 */
	public int degree(V v);
}
