package de.amr.easy.maze.alg;

import static de.amr.easy.maze.misc.MazeUtils.permute;

import java.util.stream.Stream;

import de.amr.easy.graph.api.TraversalState;
import de.amr.easy.grid.api.Grid2D;

/**
 * Creates maze as a binary tree with random cell selection.
 * 
 * @author Armin Reichert
 */
public class BinaryTreeRandom extends BinaryTree {

	public BinaryTreeRandom(Grid2D<TraversalState, Integer> grid) {
		super(grid);
	}

	@Override
	protected Stream<Integer> cellStream() {
		return permute(grid.vertexStream());
	}
}