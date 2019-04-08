package de.amr.maze.alg.mst;

import de.amr.graph.core.api.TraversalState;
import de.amr.graph.grid.api.GridGraph2D;
import de.amr.graph.pathfinder.api.Path;
import de.amr.graph.pathfinder.impl.DepthFirstSearch2;

/**
 * Reverse-Delete-MST algorithm using depth-first search for connectivity test.
 * 
 * @author Armin Reichert
 *
 * @see <a href="https://en.wikipedia.org/wiki/Reverse-delete_algorithm">Wikipedia</a>
 */
public class ReverseDeleteMST_DFS extends ReverseDeleteMST {

	public ReverseDeleteMST_DFS(GridGraph2D<TraversalState, Integer> grid) {
		super(grid);
	}

	@Override
	protected boolean connected(int u, int v) {
		return new DepthFirstSearch2(grid).findPath(u, v) != Path.NULL;
	}
}