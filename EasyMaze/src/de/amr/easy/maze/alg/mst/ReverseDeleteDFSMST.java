package de.amr.easy.maze.alg.mst;

import de.amr.easy.graph.api.TraversalState;
import de.amr.easy.graph.traversal.DepthFirstTraversal;
import de.amr.easy.grid.api.Grid2D;

/**
 * A (naive?) implementation of the Reverse-Delete-MST algorithm.
 * 
 * @author Armin Reichert
 *
 * @see <a href="https://en.wikipedia.org/wiki/Reverse-delete_algorithm">Wikipedia</a>
 */
public class ReverseDeleteDFSMST extends ReverseDeleteMST {

	public ReverseDeleteDFSMST(Grid2D<TraversalState, Integer> grid) {
		super(grid);
	}

	@Override
	protected boolean disconnected(int u, int v) {
		DepthFirstTraversal dfs = new DepthFirstTraversal(grid);
		dfs.traverseGraph(u, -1);
		return dfs.getParent(v) == -1;
	}
}