package de.amr.easy.maze.alg.mst;

import de.amr.easy.graph.impl.traversal.HillClimbing;

/**
 * A (naive?) implementation of the Reverse-Delete-MST algorithm.
 * 
 * @author Armin Reichert
 *
 * @see <a href="https://en.wikipedia.org/wiki/Reverse-delete_algorithm">Wikipedia</a>
 */
public class ReverseDeleteHillClimbingMST extends ReverseDeleteMST {

	public ReverseDeleteHillClimbingMST(int numCols, int numRows) {
		super(numCols, numRows);
	}

	@Override
	protected boolean connected(int u, int v) {
		HillClimbing<Integer> search = new HillClimbing<>(maze, x -> maze.manhattan(x, v));
		search.traverseGraph(u, v);
		return search.getParent(v) != -1;
	}
}