package de.amr.maze.alg.mst;

import de.amr.graph.core.api.TraversalState;
import de.amr.graph.grid.api.GridGraph2D;
import de.amr.graph.pathfinder.api.Path;
import de.amr.graph.pathfinder.impl.HillClimbingSearch;

/**
 * Reverse-Delete-MST algorithm using "hill climbing" for connectivity test.
 * 
 * @author Armin Reichert
 *
 * @see <a href="https://en.wikipedia.org/wiki/Reverse-delete_algorithm">Wikipedia</a>
 */
public class ReverseDeleteMST_HillClimbing extends ReverseDeleteMST {

	public ReverseDeleteMST_HillClimbing(GridGraph2D<TraversalState, Integer> grid) {
		super(grid);
	}

	@Override
	protected boolean connected(int u, int v) {
		return new HillClimbingSearch(grid, x -> grid.manhattan(x, v)).findPath(u, v) != Path.NULL;
	}
}