package de.amr.demos.maze.swingapp.model;

import de.amr.easy.graph.api.TraversalState;
import de.amr.easy.grid.api.Topology;
import de.amr.easy.grid.impl.ObservableGrid;

/**
 * Type of grid used in the maze demo application.
 * 
 * @author Armin Reichert
 */
public class MazeGrid extends ObservableGrid<TraversalState, Integer> {

	public MazeGrid(int numCols, int numRows, Topology top, TraversalState defaultContent, boolean sparse) {
		super(numCols, numRows, top, defaultContent, sparse);
	}

}
