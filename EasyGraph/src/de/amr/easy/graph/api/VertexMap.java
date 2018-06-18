package de.amr.easy.graph.api;

/**
 * Interface for a map from vertex indices to vertex objects.
 *
 * @param <V>
 *          vertex object type
 */
public interface VertexMap<V> {

	/**
	 * Returns the vertex object at the given vertex index.
	 * 
	 * @param v
	 *          a vertex index
	 * @return the object at this index or the default vertex object if no mapping exists
	 */
	V get(int v);

	/**
	 * Sets the vertex object at the given vertex index.
	 * 
	 * @param v
	 *          a vertex index
	 * @param vertex
	 *          the vertex object at this index
	 */
	void set(int v, V vertex);

	/**
	 * Clears the complete grid content.
	 */
	void clear();

	/**
	 * Sets the default vertex object
	 * 
	 * @param vertex
	 *          the default vertex object
	 */
	void setDefaultVertex(V vertex);

	/**
	 * @return the default vertex object of the graph
	 */
	V getDefaultVertex();
}