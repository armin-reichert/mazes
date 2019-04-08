package de.amr.maze.alg.traversal;

import java.util.List;

import de.amr.graph.core.api.TraversalState;
import de.amr.graph.grid.api.GridGraph2D;

/**
 * Growing tree algorithm where always the first vertex is selected from the frontier.
 * 
 * @author Armin Reichert
 */
public class GrowingTreeAlwaysFirst extends GrowingTree {

	public GrowingTreeAlwaysFirst(GridGraph2D<TraversalState, Integer> grid) {
		super(grid);
	}

	@Override
	protected int selectCell(List<Integer> frontier) {
		return frontier.remove(0);
	}
}