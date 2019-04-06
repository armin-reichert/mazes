package de.amr.maze.alg.mst;

import de.amr.graph.pathfinder.api.Path;
import de.amr.graph.pathfinder.impl.BestFirstSearch;
import de.amr.maze.alg.core.MazeGridFactory;

/**
 * Reverse-Delete-MST algorithm using best-first search for connectivity test.
 * 
 * @author Armin Reichert
 *
 * @see <a href="https://en.wikipedia.org/wiki/Reverse-delete_algorithm">Wikipedia</a>
 */
public class ReverseDeleteMST_BestFS extends ReverseDeleteMST {

	public ReverseDeleteMST_BestFS(MazeGridFactory factory, int numCols, int numRows) {
		super(factory, numCols, numRows);
	}

	@Override
	protected boolean connected(int u, int v) {
		return new BestFirstSearch(grid, x -> grid.manhattan(x, v)).findPath(u, v) != Path.NULL;
	}
}