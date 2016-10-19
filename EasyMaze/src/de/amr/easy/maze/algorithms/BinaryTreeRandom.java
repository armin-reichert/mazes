package de.amr.easy.maze.algorithms;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import de.amr.easy.graph.api.TraversalState;
import de.amr.easy.grid.api.DataGrid2D;

/**
 * Creates maze as a binary tree with random cell selection.
 * 
 * @author Armin Reichert
 */
public class BinaryTreeRandom extends BinaryTree {

	private final List<Integer> cellsInRandomOrder;

	public BinaryTreeRandom(DataGrid2D<TraversalState> grid) {
		super(grid);
		cellsInRandomOrder = grid.vertexStream().collect(Collectors.toList());
		Collections.shuffle(cellsInRandomOrder);
	}

	@Override
	protected Stream<Integer> cellStream() {
		return cellsInRandomOrder.stream();
	}
}