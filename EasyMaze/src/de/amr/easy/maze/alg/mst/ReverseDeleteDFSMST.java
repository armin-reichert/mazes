package de.amr.easy.maze.alg.mst;

import de.amr.easy.graph.api.SimpleEdge;
import de.amr.easy.graph.api.traversal.TraversalState;
import de.amr.easy.graph.impl.traversal.DepthFirstTraversal2;
import de.amr.easy.grid.api.Grid2D;

/**
 * A (naive?) implementation of the Reverse-Delete-MST algorithm.
 * 
 * @author Armin Reichert
 *
 * @see <a href="https://en.wikipedia.org/wiki/Reverse-delete_algorithm">Wikipedia</a>
 */
public class ReverseDeleteDFSMST extends ReverseDeleteMST {

	public ReverseDeleteDFSMST(Grid2D<TraversalState, SimpleEdge> grid) {
		super(grid);
	}

	@Override
	protected boolean connected(int u, int v) {
		DepthFirstTraversal2 dfs = new DepthFirstTraversal2(grid);
		dfs.traverseGraph(u, v);
		return dfs.getParent(v) != -1;
	}
}