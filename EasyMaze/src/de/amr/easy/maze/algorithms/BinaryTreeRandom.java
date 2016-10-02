package de.amr.easy.maze.algorithms;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.amr.easy.graph.api.TraversalState;
import de.amr.easy.graph.impl.DefaultEdge;
import de.amr.easy.grid.api.ObservableDataGrid2D;

public class BinaryTreeRandom extends BinaryTree {

	private final List<Integer> cellsInRandomOrder;

	public BinaryTreeRandom(ObservableDataGrid2D<Integer, DefaultEdge<Integer>, TraversalState> grid) {
		super(grid);
		cellsInRandomOrder = new ArrayList<>(grid.vertexCount());
		for (Integer cell : grid.vertexSequence()) {
			cellsInRandomOrder.add(cell);
		}
		Collections.shuffle(cellsInRandomOrder);
	}

	@Override
	protected Iterable<Integer> getCells() {
		return cellsInRandomOrder;
	}
}
