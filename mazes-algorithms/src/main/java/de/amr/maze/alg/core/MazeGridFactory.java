package de.amr.maze.alg.core;

import de.amr.graph.core.api.TraversalState;
import de.amr.graph.grid.api.GridGraph2D;

/**
 * Interface used by maze generators to create the initial grid graphs.
 * 
 * @author Armin Reichert
 */
public interface MazeGridFactory {

	/**
	 * Creates an empty grid of the given size.
	 * 
	 * @param numCols
	 *                       number of columns
	 * @param numRows
	 *                       number of rows
	 * @param defaultState
	 *                       default cell state
	 * @return empty grid (no edges)
	 */
	GridGraph2D<TraversalState, Integer> emptyGrid(int numCols, int numRows, TraversalState defaultState);

	/**
	 * Creates a full grid of the given size.
	 * 
	 * @param numCols
	 *                       number of columns
	 * @param numRows
	 *                       number of rows
	 * @param defaultState
	 *                       default cell state
	 * @return full grid (all edges)
	 */
	GridGraph2D<TraversalState, Integer> fullGrid(int numCols, int numRows, TraversalState defaultState);

}
