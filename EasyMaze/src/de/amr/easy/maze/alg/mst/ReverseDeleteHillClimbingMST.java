package de.amr.easy.maze.alg.mst;

import de.amr.easy.graph.api.SimpleEdge;
import de.amr.easy.graph.api.TraversalState;
import de.amr.easy.graph.traversal.HillClimbing;
import de.amr.easy.grid.api.Grid2D;

/**
 * A (naive?) implementation of the Reverse-Delete-MST algorithm.
 * 
 * @author Armin Reichert
 *
 * @see <a href="https://en.wikipedia.org/wiki/Reverse-delete_algorithm">Wikipedia</a>
 */
public class ReverseDeleteHillClimbingMST extends ReverseDeleteMST {

	public ReverseDeleteHillClimbingMST(Grid2D<TraversalState, SimpleEdge> grid) {
		super(grid);
	}

	@Override
	protected boolean connected(int u, int v) {
		HillClimbing<?, Integer> dfs = new HillClimbing<>(grid, x -> grid.manhattan(x, v));
		dfs.traverseGraph(u, v);
		return dfs.getParent(v) != -1;
	}
}