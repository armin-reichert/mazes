package de.amr.maze.alg.traversal;

import java.util.List;

public class GrowingTreeAlwaysLast extends GrowingTree {

	public GrowingTreeAlwaysLast(int numCols, int numRows) {
		super(numCols, numRows);
	}

	@Override
	protected int selectCell(List<Integer> frontier) {
		return frontier.remove(frontier.size() - 1);
	}
}