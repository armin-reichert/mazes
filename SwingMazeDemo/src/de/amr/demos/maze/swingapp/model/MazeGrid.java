package de.amr.demos.maze.swingapp.model;

import static de.amr.easy.graph.api.traversal.TraversalState.UNVISITED;

import de.amr.easy.graph.api.SimpleEdge;
import de.amr.easy.graph.api.traversal.TraversalState;
import de.amr.easy.grid.impl.ObservableGridGraph;
import de.amr.easy.grid.impl.Top4;

/**
 * Type of grid used in the maze demo application.
 * 
 * @author Armin Reichert
 */
public class MazeGrid extends ObservableGridGraph<TraversalState, SimpleEdge> {

	public MazeGrid(int numCols, int numRows) {
		super(numCols, numRows, Top4.get(), SimpleEdge::new);
		setDefaultVertex(UNVISITED);
	}
}