package de.amr.easy.maze.alg.core;

import static de.amr.easy.graph.api.traversal.TraversalState.COMPLETED;
import static de.amr.easy.graph.api.traversal.TraversalState.UNVISITED;
import static de.amr.easy.graph.api.traversal.TraversalState.VISITED;

import java.util.Random;
import java.util.function.IntPredicate;

import de.amr.easy.graph.api.traversal.TraversalState;
import de.amr.easy.grid.api.GridGraph2D;

/**
 * Abstract base class for maze generation algorithms.
 * 
 * @author Armin Reichert
 * 
 * @param <E>
 *          edge label type
 */
public abstract class MazeAlgorithm<E> {

	/** The grid this algorithm operates on. */
	protected final GridGraph2D<TraversalState, E> grid;

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
	public MazeAlgorithm(GridGraph2D<TraversalState, E> grid) {
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
}