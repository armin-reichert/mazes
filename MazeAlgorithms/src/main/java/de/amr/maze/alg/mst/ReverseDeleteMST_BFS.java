package de.amr.maze.alg.mst;

import de.amr.graph.pathfinder.impl.BreadthFirstSearch;

/**
 * Reverse-Delete-MST algorithm using breadth-first search for connectivity test.
 * 
 * @author Armin Reichert
 *
 * @see <a href="https://en.wikipedia.org/wiki/Reverse-delete_algorithm">Wikipedia</a>
 */
public class ReverseDeleteMST_BFS extends ReverseDeleteMST {

	public ReverseDeleteMST_BFS(int numCols, int numRows) {
		super(numCols, numRows);
	}

	@Override
	protected boolean connected(int u, int v) {
		BreadthFirstSearch<?, ?> bfs = new BreadthFirstSearch<>(grid);
		bfs.traverseGraph(u, v);
		return bfs.getParent(v) != -1;
	}
}