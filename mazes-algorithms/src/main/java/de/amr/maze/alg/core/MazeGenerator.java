package de.amr.maze.alg.core;

import de.amr.graph.core.api.TraversalState;
import de.amr.graph.grid.api.GridGraph2D;

/**
 * Common maze generator interface.
 * 
 * @author Armin Reichert
 */
public interface MazeGenerator {

	/**
	 * @return the grid this generator operates upon
	 */
	GridGraph2D<TraversalState, Integer> getGrid();

	/**
	 * Creates a maze starting at the grid cell {@code (x, y)}.
	 * 
	 * @param x
	 *            x-coordinate (column) of start cell
	 * @param y
	 *            y-coordinate (row) of start cell
	 * @return maze (spanning tree)
	 */
	GridGraph2D<TraversalState, Integer> createMaze(int x, int y);

	/**
	 * Tells if the given cell is unvisited by the maze generator.
	 * 
	 * @param cell
	 *               grid cell
	 * @return {@code true} if cell has not yet been visited
	 */
	default boolean isUnvisited(int cell) {
		return getGrid().get(cell) == TraversalState.UNVISITED;
	}

	/**
	 * Tells if the given cell has already been visited by the maze generator.
	 * 
	 * @param cell
	 *               grid cell
	 * @return {@code true} if cell has already been visited
	 */
	default boolean isVisited(int v) {
		return getGrid().get(v) == TraversalState.VISITED;
	}

	/**
	 * Tells if the given cell has been completed by the maze generator.
	 * 
	 * @param cell
	 *               grid cell
	 * @return {@code true} if cell has been completed
	 */
	default boolean isCompleted(int v) {
		return getGrid().get(v) == TraversalState.COMPLETED;
	}
}