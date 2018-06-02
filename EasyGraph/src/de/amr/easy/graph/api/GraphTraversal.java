package de.amr.easy.graph.api;

/**
 * Common interface for graph traversals.
 * 
 * @author Armin Reichert
 */
public interface GraphTraversal {

	/**
	 * Traverses the graph starting from the given source and ending at the given target vertex.
	 * 
	 * @param source
	 *          source vertex
	 * @param target
	 *          target vertex or {@code -1} if the complete graph (or connected component) shall be
	 *          traversed
	 */
	void traverseGraph(int source, int target);

	/**
	 * Traverses the complete graph (connected component) starting from the given source vertex.
	 * 
	 * @param source
	 *          source vertex
	 */
	default void traverseGraph(int source) {
		traverseGraph(source, -1);
	}

	/**
	 * Returns the parent vertex of the given vertex i.e. the vertex from which the given vertex was
	 * reached over an edge.
	 * 
	 * @param vertex
	 *          a vertex
	 * @return the parent vertex or {@code -1} if there is no such vertex or the traversal has not yet
	 *         been done
	 */
	int getParent(int vertex);

	/**
	 * Returns the traversal state of the given vertex (UNVISITED, VISITED or COMPLETED).
	 * 
	 * @param vertex
	 *          a vertex
	 * @return the traversal state
	 */
	TraversalState getState(int vertex);

	/**
	 * Tells if the given vertex is currently in the queue (FIFO-queue, stack, priority queue)
	 * 
	 * @param vertex
	 *          a vertex
	 * @return {@code true} if the vertex is currently in the queue
	 */
	boolean inQ(int vertex);
}