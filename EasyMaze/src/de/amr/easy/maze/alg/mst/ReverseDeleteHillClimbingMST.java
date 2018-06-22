package de.amr.easy.maze.alg.mst;

import de.amr.easy.graph.impl.traversal.HillClimbing;
import de.amr.easy.maze.alg.core.OrthogonalGrid;

/**
 * A (naive?) implementation of the Reverse-Delete-MST algorithm.
 * 
 * @author Armin Reichert
 *
 * @see <a href="https://en.wikipedia.org/wiki/Reverse-delete_algorithm">Wikipedia</a>
 */
public class ReverseDeleteHillClimbingMST extends ReverseDeleteMST {

	public ReverseDeleteHillClimbingMST(OrthogonalGrid grid) {
		super(grid);
	}

	@Override
	protected boolean connected(int u, int v) {
		HillClimbing<Integer> search = new HillClimbing<>(grid, x -> grid.manhattan(x, v));
		search.traverseGraph(u, v);
		return search.getParent(v) != -1;
	}
}