package de.amr.easy.graph.api;

import java.util.function.BiFunction;

/**
 * Interface for labeling graph edges.
 * 
 * @author Armin Reichert
 *
 * @param <E>
 *          edge label type
 */
public interface EdgeLabels<E> {

	/**
	 * Returns the edge label for the edge with the given end points.
	 * 
	 * @param u
	 *            either end of edge
	 * @param v
	 *            other end of edge
	 * @return the edge label for this edge if defined or the default edge label
	 */
	E getEdgeLabel(int u, int v);

	/**
	 * Sets the edge label for the edge with the given end points.
	 * 
	 * @param u
	 *            either end of edge
	 * @param v
	 *            other end of edge
	 * @param e
	 *            edge label
	 */
	void setEdgeLabel(int u, int v, E e);

	/**
	 * Clears the complete edge content.
	 */
	void clearEdgeLabels();

	/**
	 * Sets the default edge label.
	 * 
	 * @param fnDefaultLabel
	 *                         function providing the default edge label
	 */
	void setDefaultEdgeLabel(BiFunction<Integer, Integer, E> fnDefaultLabel);

	/**
	 * @return the default edge label of the graph
	 */
	E getDefaultEdgeLabel(int u, int v);
}
