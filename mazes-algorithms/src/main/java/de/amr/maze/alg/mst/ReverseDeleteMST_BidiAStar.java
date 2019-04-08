package de.amr.maze.alg.mst;

import de.amr.graph.core.api.TraversalState;
import de.amr.graph.grid.api.GridGraph2D;
import de.amr.graph.pathfinder.api.Path;
import de.amr.graph.pathfinder.impl.BidiAStarSearch;

/**
 * Reverse-Delete-MST algorithm using bidirectional A* for connectivity test.
 * 
 * @author Armin Reichert
 *
 * @see <a href="https://en.wikipedia.org/wiki/Reverse-delete_algorithm">Wikipedia</a>
 */
public class ReverseDeleteMST_BidiAStar extends ReverseDeleteMST {

	public ReverseDeleteMST_BidiAStar(GridGraph2D<TraversalState, Integer> grid) {
		super(grid);
	}

	@Override
	protected boolean connected(int u, int v) {
		return new BidiAStarSearch(grid, (v1, v2) -> 1.0, grid::euclidean, grid::euclidean).findPath(u,
				v) != Path.NULL;
	}
}