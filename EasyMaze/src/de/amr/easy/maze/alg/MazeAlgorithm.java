package de.amr.easy.maze.alg;

import static de.amr.easy.graph.api.TraversalState.COMPLETED;

import java.util.Random;
import java.util.function.IntPredicate;

import de.amr.easy.graph.api.TraversalState;
import de.amr.easy.grid.api.Grid2D;

/**
 * Abstract base class for maze generation algorithms.
 * 
 * @author Armin Reichert
 */
public abstract class MazeAlgorithm {

	protected final Grid2D<TraversalState, Integer> grid;
	protected final Random rnd;
	protected final IntPredicate isCellUnvisited;
	protected final IntPredicate isCellVisited;
	protected final IntPredicate isCellCompleted;

	public MazeAlgorithm(Grid2D<TraversalState, Integer> grid) {
		this.grid = grid;
		this.rnd = new Random();
		this.isCellUnvisited = cell -> grid.get(cell) == TraversalState.UNVISITED;
		this.isCellVisited = cell -> grid.get(cell) == TraversalState.VISITED;
		this.isCellCompleted = cell -> grid.get(cell) == TraversalState.COMPLETED;
	}

	/**
	 * Runs the maze generation starting from the given cell. The maze algorithm of course can ignore
	 * this and select another cell.
	 * 
	 * @param startCell
	 *          the grid cell where the generation starts
	 */
	public abstract void run(int startCell);

	/**
	 * Can be overridden by subclasses of a maze generation algorithm to specify a different start
	 * cell.
	 * 
	 * @param startCell
	 *          the original start cell passed to the algorithm
	 * @return the possibly modified start cell
	 */
	protected int customizedStartCell(int startCell) {
		return startCell;
	}

	/**
	 * Adds a grid edge between the given vertices and marks them as completed.
	 * 
	 * @param u
	 *          a grid vertex (cell)
	 * @param v
	 *          a grid vertex (cell)
	 */
	protected final void addEdge(int u, int v) {
		grid.addEdge(u, v);
		grid.set(u, COMPLETED);
		grid.set(v, COMPLETED);
	}
}