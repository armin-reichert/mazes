package de.amr.maze.alg.traversal;

import java.util.List;
import java.util.Random;

/**
 * The "Growing-Tree" algorithm.
 * 
 * @author Armin Reichert
 * 
 * @see <a href=
 *      "http://weblog.jamisbuck.org/2011/1/27/maze-generation-growing-tree-algorithm.html">Maze
 *      Generation: Growing Tree algorithm</a>
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