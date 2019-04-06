package de.amr.maze.alg;

import static de.amr.datastruct.StreamUtils.permute;

import java.util.stream.IntStream;

import de.amr.maze.alg.core.MazeGridFactory;

/**
 * Creates maze as a binary tree with random cell selection.
 * 
 * @author Armin Reichert
 */
public class BinaryTreeRandom extends BinaryTree {

	public BinaryTreeRandom(MazeGridFactory factory, int numCols, int numRows) {
		super(factory, numCols, numRows);
	}

	@Override
	protected IntStream cells() {
		return permute(grid.vertices());
	}
}