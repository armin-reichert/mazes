package de.amr.easy.maze.alg.mst;

import de.amr.easy.graph.impl.traversal.BreadthFirstTraversal;

/**
 * A (naive?) implementation of the Reverse-Delete-MST algorithm.
 * 
 * @author Armin Reichert
 *
 * @see <a href="https://en.wikipedia.org/wiki/Reverse-delete_algorithm">Wikipedia</a>
 */
public class ReverseDeleteBFSMST extends ReverseDeleteMST {

	public ReverseDeleteBFSMST(int numCols, int numRows) {
		super(numCols, numRows);
	}

	@Override
	protected boolean connected(int u, int v) {
		BreadthFirstTraversal bfs = new BreadthFirstTraversal(maze);
		bfs.traverseGraph(u, v);
		return bfs.getParent(v) != -1;
	}
}