package de.amr.easy.maze.alg.core;

import static de.amr.easy.maze.alg.core.OrthogonalGrid.emptyGrid;
import static de.amr.easy.maze.alg.core.OrthogonalGrid.fullGrid;

import de.amr.easy.graph.api.traversal.TraversalState;

/**
 * Maze generator base class.
 * 
 * @author Armin Reichert
 */
public abstract class OrthogonalMazeGenerator {

	protected OrthogonalGrid maze;

	protected OrthogonalMazeGenerator(int numCols, int numRows, boolean full, TraversalState defaultState) {
		maze = full ? fullGrid(numCols, numRows, defaultState) : emptyGrid(numCols, numRows, defaultState);
	}

	protected OrthogonalMazeGenerator(OrthogonalGrid maze) {
		this.maze = maze;
	}

	public OrthogonalGrid getGrid() {
		return maze;
	}
	
	public abstract OrthogonalGrid createMaze(int x, int y);
}