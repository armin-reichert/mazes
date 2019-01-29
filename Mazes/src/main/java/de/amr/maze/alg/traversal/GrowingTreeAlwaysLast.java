package de.amr.maze.alg.traversal;

import java.util.List;

/**
 * Growing tree algorithm where always the last vertex selected from the frontier.
 * 
 * @author Armin Reichert
 */
public class GrowingTreeAlwaysLast extends GrowingTree {

	public GrowingTreeAlwaysLast(int numCols, int numRows) {
		super(numCols, numRows);
	}

	@Override
	protected int selectCell(List<Integer> frontier) {
		return frontier.remove(frontier.size() - 1);
	}
}