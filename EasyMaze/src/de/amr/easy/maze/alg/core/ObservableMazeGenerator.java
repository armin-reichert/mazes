package de.amr.easy.maze.alg.core;

import de.amr.easy.graph.api.traversal.TraversalState;

/**
 * Maze generator base class.
 * 
 * @author Armin Reichert
 */
public abstract class ObservableMazeGenerator implements MazeGenerator {

	protected OrthogonalGrid maze;

	protected ObservableMazeGenerator(int numCols, int numRows, boolean full, TraversalState defaultState) {
		if (full) {
			maze = fullGrid(numCols, numRows, defaultState);
		} else {
			maze = emptyGrid(numCols, numRows, defaultState);
		}
	}
	
	protected ObservableMazeGenerator(OrthogonalGrid maze) {
		this.maze = maze;
	}

	public OrthogonalGrid getGrid() {
		return maze;
	}

	public static OrthogonalGrid emptyGrid(int numCols, int numRows, TraversalState defaultState) {
		OrthogonalGrid grid = new OrthogonalGrid(numCols, numRows, defaultState);
		return grid;
	}

	public static OrthogonalGrid fullGrid(int numCols, int numRows, TraversalState defaultState) {
		OrthogonalGrid grid = new OrthogonalGrid(numCols, numRows, defaultState);
		grid.fill();
		return grid;
	}
}