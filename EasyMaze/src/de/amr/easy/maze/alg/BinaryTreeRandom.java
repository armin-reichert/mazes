package de.amr.easy.maze.alg;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import de.amr.easy.graph.api.TraversalState;
import de.amr.easy.grid.api.Grid2D;
import de.amr.easy.grid.api.dir.Dir4;

/**
 * Creates maze as a binary tree with random cell selection.
 * 
 * @author Armin Reichert
 */
public class BinaryTreeRandom extends BinaryTree {

	private final List<Integer> cellsInRandomOrder;

	public BinaryTreeRandom(Grid2D<Dir4,TraversalState, Integer> grid) {
		super(grid);
		cellsInRandomOrder = grid.vertexStream().collect(Collectors.toList());
		Collections.shuffle(cellsInRandomOrder);
	}

	@Override
	protected Stream<Integer> cellStream() {
		return cellsInRandomOrder.stream();
	}
}