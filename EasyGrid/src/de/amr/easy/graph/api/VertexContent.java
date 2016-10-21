package de.amr.easy.graph.api;

/**
 * Interface for accessing content stored in a graph vertex.
 *
 * @param <Content>
 *          vertex content type
 */
public interface VertexContent<V, Content> {

	/**
	 * Returns the content of the given vertex.
	 * 
	 * @param vertex
	 *          a vertex
	 * @return the content or the default content if no content has been set for this vertex
	 */
	public Content get(V vertex);

	/**
	 * Sets the content of the given vertex.
	 * 
	 * @param vertex
	 *          a vertex
	 * @param data
	 *          the vertex content
	 */
	public void set(V vertex, Content data);

	/**
	 * Clears the complete grid content.
	 */
	public void clear();

	/**
	 * Sets the default content for the cells
	 * 
	 * @param data
	 *          the default content
	 */
	public void setDefault(Content data);
}
