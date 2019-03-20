package de.amr.maze.alg.traversal;

import java.util.List;
import java.util.Random;

/**
 * Growing tree algorithm where either the last or a random vertex is selected from the frontier.
 * 
 * @author Armin Reichert
 */
public class GrowingTreeLastOrRandom extends GrowingTree {

	private Random rnd = new Random();

	public GrowingTreeLastOrRandom(int numCols, int numRows) {
		super(numCols, numRows);
	}

	@Override
	protected int selectCell(List<Integer> frontier) {
		return frontier.remove(rnd.nextBoolean() ? frontier.size() - 1 : rnd.nextInt(frontier.size()));
	}
}