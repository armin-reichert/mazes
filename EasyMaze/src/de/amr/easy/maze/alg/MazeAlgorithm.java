package de.amr.easy.maze.alg;

import static de.amr.easy.graph.api.TraversalState.COMPLETED;
import static de.amr.easy.graph.api.TraversalState.UNVISITED;
import static de.amr.easy.graph.api.TraversalState.VISITED;

import java.util.Random;

import de.amr.easy.graph.api.TraversalState;
import de.amr.easy.graph.api.WeightedEdge;
import de.amr.easy.grid.api.Grid2D;

/**
 * Abstract base class for maze generation algorithms.
 * 
 * @author Armin Reichert
 */
public abstract class MazeAlgorithm {

	protected final Grid2D<TraversalState, Integer> grid;
	protected final Random rnd = new Random();

	public MazeAlgorithm(Grid2D<TraversalState, Integer> grid) {
		this.grid = grid;
	}

	/**
	 * Runs the maze generation starting from the given cell. The maze algorithm of course can ignore
	 * this and select another cell.
	 * 
	 * @param startCell
	 *          the grid cell where the generation starts
	 */
	public abstract void run(Integer startCell);

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

	protected final boolean isCellUnvisited(int cell) {
		return grid.get(cell) == UNVISITED;
	}

	protected final boolean isCellVisited(int cell) {
		return grid.get(cell) == VISITED;
	}

	protected final boolean isCellCompleted(int cell) {
		return grid.get(cell) == COMPLETED;
	}

	protected final WeightedEdge<Integer> setRandomEdgeWeight(WeightedEdge<Integer> edge) {
		edge.setWeight(rnd.nextInt());
		return edge;
	}
}