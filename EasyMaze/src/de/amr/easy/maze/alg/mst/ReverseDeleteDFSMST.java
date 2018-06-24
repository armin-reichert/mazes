package de.amr.easy.maze.alg.mst;

import de.amr.easy.graph.impl.traversal.DepthFirstTraversal2;

/**
 * A (naive?) implementation of the Reverse-Delete-MST algorithm.
 * 
 * @author Armin Reichert
 *
 * @see <a href="https://en.wikipedia.org/wiki/Reverse-delete_algorithm">Wikipedia</a>
 */
public class ReverseDeleteDFSMST extends ReverseDeleteMST {

	public ReverseDeleteDFSMST(int numCols, int numRows) {
		super(numCols, numRows);
	}

	@Override
	protected boolean connected(int u, int v) {
		DepthFirstTraversal2 dfs = new DepthFirstTraversal2(maze);
		dfs.traverseGraph(u, v);
		return dfs.getParent(v) != -1;
	}
}