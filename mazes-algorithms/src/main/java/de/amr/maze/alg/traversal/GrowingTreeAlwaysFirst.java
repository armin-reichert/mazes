package de.amr.maze.alg.traversal;

import java.util.List;

import de.amr.maze.alg.core.MazeGridFactory;

/**
 * Growing tree algorithm where always the first vertex is selected from the frontier.
 * 
 * @author Armin Reichert
 */
public class GrowingTreeAlwaysFirst extends GrowingTree {

	public GrowingTreeAlwaysFirst(MazeGridFactory factory, int numCols, int numRows) {
		super(factory, numCols, numRows);
	}

	@Override
	protected int selectCell(List<Integer> frontier) {
		return frontier.remove(0);
	}
}