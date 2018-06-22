package de.amr.easy.maze.alg;

import static de.amr.easy.util.StreamUtils.permute;

import java.util.stream.IntStream;

import de.amr.easy.graph.api.traversal.TraversalState;
import de.amr.easy.grid.api.GridGraph2D;

/**
 * Creates maze as a binary tree with random cell selection.
 * 
 * @author Armin Reichert
 */
public class BinaryTreeRandom extends BinaryTree {

	public BinaryTreeRandom(GridGraph2D<TraversalState, Void> grid) {
		super(grid);
	}

	@Override
	protected IntStream cellStream() {
		return permute(grid.vertices());
	}
}