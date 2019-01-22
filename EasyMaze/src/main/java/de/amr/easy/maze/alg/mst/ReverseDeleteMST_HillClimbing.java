package de.amr.easy.maze.alg.mst;

import de.amr.easy.graph.pathfinder.impl.HillClimbingPathFinder;

/**
 * Reverse-Delete-MST algorithm using "hill climbing" for connectivity test.
 * 
 * @author Armin Reichert
 *
 * @see <a href="https://en.wikipedia.org/wiki/Reverse-delete_algorithm">Wikipedia</a>
 */
public class ReverseDeleteMST_HillClimbing extends ReverseDeleteMST {

	public ReverseDeleteMST_HillClimbing(int numCols, int numRows) {
		super(numCols, numRows);
	}

	@Override
	protected boolean connected(int u, int v) {
		HillClimbingPathFinder<Integer> search = new HillClimbingPathFinder<>(grid, x -> grid.manhattan(x, v));
		search.traverseGraph(u, v);
		return search.getParent(v) != -1;
	}
}