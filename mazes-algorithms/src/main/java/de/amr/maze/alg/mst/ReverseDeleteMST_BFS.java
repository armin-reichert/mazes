package de.amr.maze.alg.mst;

import de.amr.graph.core.api.TraversalState;
import de.amr.graph.grid.api.GridGraph2D;
import de.amr.graph.pathfinder.api.Path;
import de.amr.graph.pathfinder.impl.BreadthFirstSearch;

/**
 * Reverse-Delete-MST algorithm using breadth-first search for connectivity test.
 * 
 * @author Armin Reichert
 *
 * @see <a href="https://en.wikipedia.org/wiki/Reverse-delete_algorithm">Wikipedia</a>
 */
public class ReverseDeleteMST_BFS extends ReverseDeleteMST {

	public ReverseDeleteMST_BFS(GridGraph2D<TraversalState, Integer> grid) {
		super(grid);
	}

	@Override
	protected boolean connected(int u, int v) {
		return new BreadthFirstSearch(grid).findPath(u, v) != Path.NULL;
	}
}