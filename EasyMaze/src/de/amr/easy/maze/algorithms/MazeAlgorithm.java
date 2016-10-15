package de.amr.easy.maze.algorithms;

import java.util.Random;
import java.util.function.Consumer;

import de.amr.easy.graph.api.TraversalState;
import de.amr.easy.grid.api.ObservableDataGrid2D;

public abstract class MazeAlgorithm implements Consumer<Integer> {

	protected final ObservableDataGrid2D<TraversalState> grid;
	protected final Random rnd = new Random();

	public MazeAlgorithm(ObservableDataGrid2D<TraversalState> grid) {
		this.grid = grid;
	}
}