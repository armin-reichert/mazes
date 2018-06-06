package de.amr.easy.maze.alg.mst;

import de.amr.easy.graph.api.TraversalState;
import de.amr.easy.graph.traversal.BreadthFirstTraversal;
import de.amr.easy.grid.api.Grid2D;

/**
 * A (naive?) implementation of the Reverse-Delete-MST algorithm.
 * 
 * @author Armin Reichert
 *
 * @see <a href="https://en.wikipedia.org/wiki/Reverse-delete_algorithm">Wikipedia</a>
 */
public class ReverseDeleteBFSMST extends ReverseDeleteMST {

	public ReverseDeleteBFSMST(Grid2D<TraversalState, Integer> grid) {
		super(grid);
	}

	@Override
	protected boolean connected(int u, int v) {
		BreadthFirstTraversal bfs = new BreadthFirstTraversal(grid);
		bfs.traverseGraph(u);
		return bfs.getParent(v) != -1;
	}
}