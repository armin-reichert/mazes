package de.amr.easy.maze.alg.core;

import static de.amr.easy.graph.api.TraversalState.COMPLETED;
import static de.amr.easy.graph.api.TraversalState.UNVISITED;
import static de.amr.easy.graph.api.TraversalState.VISITED;

import java.util.Random;
import java.util.function.IntPredicate;

import de.amr.easy.graph.api.Edge;
import de.amr.easy.graph.api.TraversalState;
import de.amr.easy.grid.api.Grid2D;

/**
 * Abstract base class for maze generation algorithms.
 * 
 * @author Armin Reichert
 * 
 * @param <E>
 *          edge type
 */
public abstract class MazeAlgorithm<E extends Edge> {

	/** The grid this algorithm operates on. */
	protected final Grid2D<TraversalState, E> grid;

	/** A random number generator. */
	protected final Random rnd;

	protected final IntPredicate isCellUnvisited;
	protected final IntPredicate isCellVisited;
	protected final IntPredicate isCellCompleted;

	/**
	 * Creates an instance of a maze generator.
	 * 
	 * @param grid
	 *          the grid this generator works on
	 */
	public MazeAlgorithm(Grid2D<TraversalState, E> grid) {
		this.grid = grid;
		this.rnd = new Random();
		this.isCellUnvisited = cell -> grid.get(cell) == UNVISITED;
		this.isCellVisited = cell -> grid.get(cell) == VISITED;
		this.isCellCompleted = cell -> grid.get(cell) == COMPLETED;
	}

	/**
	 * Runs the maze generation starting from the given cell. The maze algorithm of course can ignore
	 * this and select another cell.
	 * 
	 * @param start
	 *          the grid cell where the generation starts
	 */
	public abstract void run(int start);

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
	 * Adds an edge to the spanning tree and marks the vertices as completed.
	 * 
	 * @param u
	 *          some vertex (cell)
	 * @param v
	 *          another vertex (cell)
	 */
	protected final void addTreeEdge(int u, int v) {
		grid.addEdge(u, v);
		grid.set(u, COMPLETED);
		grid.set(v, COMPLETED);
	}
}