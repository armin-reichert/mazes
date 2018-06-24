package de.amr.easy.maze.alg.mst;

import de.amr.easy.graph.impl.traversal.BestFirstTraversal;

/**
 * A (naive?) implementation of the Reverse-Delete-MST algorithm.
 * 
 * @author Armin Reichert
 *
 * @see <a href="https://en.wikipedia.org/wiki/Reverse-delete_algorithm">Wikipedia</a>
 */
public class ReverseDeleteBestFSMST extends ReverseDeleteMST {

	public ReverseDeleteBestFSMST(int numCols, int numRows) {
		super(numCols, numRows);
	}

	@Override
	protected boolean connected(int u, int v) {
		BestFirstTraversal<Integer> bfs = new BestFirstTraversal<>(maze, x -> maze.manhattan(x, v));
		bfs.traverseGraph(u, v);
		return bfs.getParent(v) != -1;
	}
}