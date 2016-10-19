package de.amr.easy.maze.algorithms;

import java.util.Random;
import java.util.function.Consumer;

import de.amr.easy.graph.api.TraversalState;
import de.amr.easy.grid.api.DataGrid2D;

public abstract class MazeAlgorithm implements Consumer<Integer> {

	protected final DataGrid2D<TraversalState> grid;
	protected final Random rnd = new Random();

	public MazeAlgorithm(DataGrid2D<TraversalState> grid) {
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
}