package de.amr.maze.alg.traversal;

import java.util.List;

/**
 * Growing tree algorithm where always the first vertex selected from the frontier.
 * 
 * @author Armin Reichert
 */
public class GrowingTreeAlwaysFirst extends GrowingTree {

	public GrowingTreeAlwaysFirst(int numCols, int numRows) {
		super(numCols, numRows);
	}

	@Override
	protected int selectCell(List<Integer> frontier) {
		return frontier.remove(0);
	}
}