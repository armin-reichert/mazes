package de.amr.maze.alg.traversal;

import java.util.List;

public class GrowingTreeAlwaysFirst extends GrowingTree {

	public GrowingTreeAlwaysFirst(int numCols, int numRows) {
		super(numCols, numRows);
	}

	@Override
	protected int selectCell(List<Integer> frontier) {
		return frontier.remove(0);
	}
}