package de.amr.easy.maze.alg.mst;

import de.amr.easy.graph.pathfinder.impl.BestFirstSearch;

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
		BestFirstSearch<?, ?, Integer> bfs = new BestFirstSearch<>(grid,
				x -> grid.manhattan(x, v));
		bfs.traverseGraph(u, v);
		return bfs.getParent(v) != -1;
	}
}