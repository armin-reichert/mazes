package de.amr.easy.maze.algorithms;

import static de.amr.easy.graph.api.TraversalState.COMPLETED;

import java.util.Optional;
import java.util.Random;
import java.util.function.Consumer;
import java.util.stream.Stream;

import de.amr.easy.graph.api.TraversalState;
import de.amr.easy.graph.impl.DefaultEdge;
import de.amr.easy.grid.api.Direction;
import de.amr.easy.grid.api.ObservableDataGrid2D;

/**
 * Creates a random binary spanning tree.
 * 
 * @author Armin Reichert
 */
public class BinaryTree implements Consumer<Integer> {

	protected final ObservableDataGrid2D<Integer, DefaultEdge<Integer>, TraversalState> grid;

	private final Random rnd = new Random();

	public BinaryTree(ObservableDataGrid2D<Integer, DefaultEdge<Integer>, TraversalState> grid) {
		this.grid = grid;
	}

	@Override
	public void accept(Integer start) {
		getCells().forEach(cell -> {
			getRandomNeighbor(cell, Direction.S, Direction.E).ifPresent(neighbor -> {
				grid.addEdge(new DefaultEdge<>(cell, neighbor));
				grid.set(cell, COMPLETED);
				grid.set(neighbor, COMPLETED);
			});
		});
	}

	private Optional<Integer> getRandomNeighbor(Integer cell, Direction d1, Direction d2) {
		boolean b = rnd.nextBoolean();
		Integer neighbor = grid.neighbor(cell, b ? d1 : d2);
		return neighbor != null ? Optional.of(neighbor) : Optional.ofNullable(grid.neighbor(cell, b ? d2 : d1));
	}

	/*
	 * Overridden by subclass to specify different cell iteration.
	 */
	protected Stream<Integer> getCells() {
		return grid.vertexStream();
	}
}
