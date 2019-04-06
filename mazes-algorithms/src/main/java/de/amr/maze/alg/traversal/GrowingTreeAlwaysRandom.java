package de.amr.maze.alg.traversal;

import java.util.List;
import java.util.Random;

import de.amr.maze.alg.core.MazeGridFactory;

/**
 * Growing tree algorithm where always a random vertex is selected from the frontier.
 * 
 * @author Armin Reichert
 */
public class GrowingTreeAlwaysRandom extends GrowingTree {

	private final Random rnd = new Random();

	public GrowingTreeAlwaysRandom(MazeGridFactory factory, int numCols, int numRows) {
		super(factory, numCols, numRows);
	}

	@Override
	protected int selectCell(List<Integer> frontier) {
		return frontier.remove(rnd.nextInt(frontier.size()));
	}
}