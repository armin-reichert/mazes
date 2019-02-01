package de.amr.maze.alg.mst;

import de.amr.graph.pathfinder.impl.AbstractSearch;
import de.amr.graph.pathfinder.impl.BestFirstSearch;

/**
 * Reverse-Delete-MST algorithm using best-first search for connectivity test.
 * 
 * @author Armin Reichert
 *
 * @see <a href="https://en.wikipedia.org/wiki/Reverse-delete_algorithm">Wikipedia</a>
 */
public class ReverseDeleteMST_BestFS extends ReverseDeleteMST {

	public ReverseDeleteMST_BestFS(int numCols, int numRows) {
		super(numCols, numRows);
	}

	@Override
	protected boolean connected(int u, int v) {
		AbstractSearch<?, ?> best = new BestFirstSearch<>(grid, x -> grid.manhattan(x, v));
		best.traverseGraph(u, v);
		return best.getParent(v) != -1;
	}
}