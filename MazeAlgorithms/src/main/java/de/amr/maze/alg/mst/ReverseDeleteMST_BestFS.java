package de.amr.maze.alg.mst;

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
		BestFirstSearch<?, ?> bestFirstSearch = new BestFirstSearch<>(grid, x -> grid.manhattan(x, v));
		bestFirstSearch.exploreGraph(u, v);
		return bestFirstSearch.getParent(v) != -1;
	}
}