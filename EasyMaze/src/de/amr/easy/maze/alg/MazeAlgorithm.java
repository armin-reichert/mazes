package de.amr.easy.maze.alg;

import static de.amr.easy.graph.api.TraversalState.COMPLETED;
import static de.amr.easy.graph.api.TraversalState.UNVISITED;
import static de.amr.easy.graph.api.TraversalState.VISITED;

import java.util.Random;
import java.util.function.Consumer;

import de.amr.easy.graph.api.TraversalState;
import de.amr.easy.graph.api.WeightedEdge;
import de.amr.easy.grid.api.Grid2D;

public abstract class MazeAlgorithm implements Consumer<Integer> {

	protected final Grid2D<TraversalState, Integer> grid;
	protected final Random rnd = new Random();

	public MazeAlgorithm(Grid2D<TraversalState, Integer> grid) {
		this.grid = grid;
	}

	/**
	 * Can be overridden by subclasses of a maze generation algorithm to specify a different start
	 * cell.
	 * 
	 * @param originalStartCell
	 *          the original start cell passed to the algorithm
	 * @return the possibly modified start cell
	 */
	protected Integer customStartCell(Integer originalStartCell) {
		return originalStartCell;
	}

	protected boolean isCellUnvisited(Integer cell) {
		return grid.get(cell) == UNVISITED;
	}

	protected boolean isCellVisited(Integer cell) {
		return grid.get(cell) == VISITED;
	}

	protected boolean isCellCompleted(Integer cell) {
		return grid.get(cell) == COMPLETED;
	}

	protected WeightedEdge<Integer, Integer> setRandomEdgeWeight(WeightedEdge<Integer, Integer> edge) {
		edge.setWeight(rnd.nextInt());
		return edge;
	}

}