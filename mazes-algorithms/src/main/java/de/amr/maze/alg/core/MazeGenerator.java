package de.amr.maze.alg.core;

import java.util.Random;

import de.amr.graph.core.api.TraversalState;
import de.amr.graph.grid.api.GridGraph2D;

/**
 * Common maze generator interface.
 * 
 * @author Armin Reichert
 */
public abstract class MazeGenerator {

	protected final GridGraph2D<TraversalState, Integer> grid;
	protected final Random rnd = new Random();

	public MazeGenerator(GridGraph2D<TraversalState, Integer> grid) {
		this.grid = grid;
	}

	/**
	 * Creates a maze starting at the grid cell {@code (x, y)}.
	 * 
	 * @param x
	 *            x-coordinate (column) of start cell
	 * @param y
	 *            y-coordinate (row) of start cell
	 * @return maze (spanning tree)
	 */
	public abstract void createMaze(int x, int y);

	protected GridGraph2D<TraversalState, Integer> emptyGrid(int numCols, int numRows,
			TraversalState defaultState) {
		return UnobservableGridFactory.get().emptyGrid(numCols, numRows, defaultState);
	}

	protected GridGraph2D<TraversalState, Integer> fullGrid(int numCols, int numRows,
			TraversalState defaultState) {
		return UnobservableGridFactory.get().fullGrid(numCols, numRows, defaultState);
	}

	/**
	 * Tells if the given cell is unvisited by the maze generator.
	 * 
	 * @param cell
	 *               grid cell
	 * @return {@code true} if cell has not yet been visited
	 */
	protected boolean isCellUnvisited(int cell) {
		return grid.get(cell) == TraversalState.UNVISITED;
	}

	/**
	 * Tells if the given cell has already been visited by the maze generator.
	 * 
	 * @param cell
	 *               grid cell
	 * @return {@code true} if cell has already been visited
	 */
	protected boolean isCellVisited(int cell) {
		return grid.get(cell) == TraversalState.VISITED;
	}

	/**
	 * Tells if the given cell has been completed by the maze generator.
	 * 
	 * @param cell
	 *               grid cell
	 * @return {@code true} if cell has been completed
	 */
	protected boolean isCellCompleted(int cell) {
		return grid.get(cell) == TraversalState.COMPLETED;
	}

}