package de.amr.easy.maze.alg.core;

import static de.amr.easy.maze.alg.core.OrthogonalGrid.emptyGrid;
import static de.amr.easy.maze.alg.core.OrthogonalGrid.fullGrid;

import de.amr.easy.graph.api.traversal.TraversalState;

/**
 * Maze generator base class.
 * 
 * @author Armin Reichert
 */
public abstract class ObservableMazeGenerator implements MazeGenerator {

	protected OrthogonalGrid maze;

	protected ObservableMazeGenerator(int numCols, int numRows, boolean full, TraversalState defaultState) {
		maze = full ? fullGrid(numCols, numRows, defaultState) : emptyGrid(numCols, numRows, defaultState);
	}

	protected ObservableMazeGenerator(OrthogonalGrid maze) {
		this.maze = maze;
	}

	public OrthogonalGrid getGrid() {
		return maze;
	}
}