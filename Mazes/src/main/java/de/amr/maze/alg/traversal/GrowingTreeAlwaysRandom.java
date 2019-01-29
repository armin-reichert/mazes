package de.amr.maze.alg.traversal;

import java.util.List;
import java.util.Random;

public class GrowingTreeAlwaysRandom extends GrowingTree {

	private final Random rnd = new Random();

	public GrowingTreeAlwaysRandom(int numCols, int numRows) {
		super(numCols, numRows);
	}

	@Override
	protected int selectCell(List<Integer> frontier) {
		return frontier.remove(rnd.nextInt(frontier.size()));
	}
}