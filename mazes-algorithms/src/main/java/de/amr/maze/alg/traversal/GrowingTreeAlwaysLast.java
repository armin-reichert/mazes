package de.amr.maze.alg.traversal;

import java.util.List;

import de.amr.maze.alg.core.MazeGridFactory;

/**
 * Growing tree algorithm where always the last vertex is selected from the frontier.
 * 
 * @author Armin Reichert
 */
public class GrowingTreeAlwaysLast extends GrowingTree {

	public GrowingTreeAlwaysLast(MazeGridFactory factory, int numCols, int numRows) {
		super(factory, numCols, numRows);
	}

	@Override
	protected int selectCell(List<Integer> frontier) {
		return frontier.remove(frontier.size() - 1);
	}
}