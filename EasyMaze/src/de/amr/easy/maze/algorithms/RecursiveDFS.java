package de.amr.easy.maze.algorithms;

import static de.amr.easy.graph.api.TraversalState.COMPLETED;
import static de.amr.easy.graph.api.TraversalState.UNVISITED;
import static de.amr.easy.graph.api.TraversalState.VISITED;

import java.util.function.Consumer;

import de.amr.easy.graph.api.TraversalState;
import de.amr.easy.graph.impl.DefaultEdge;
import de.amr.easy.grid.api.ObservableDataGrid2D;

/**
 * Maze generator using randomized recursive depth-first-search. Not suited for larger grids because
 * of stack overflow.
 * 
 * @author Armin Reichert
 */
public class RecursiveDFS implements Consumer<Integer> {

	private final ObservableDataGrid2D<Integer, DefaultEdge<Integer>, TraversalState> grid;
	private Integer neighbor;

	public RecursiveDFS(ObservableDataGrid2D<Integer, DefaultEdge<Integer>, TraversalState> grid) {
		this.grid = grid;
	}

	@Override
	public void accept(Integer cell) {
		grid.set(cell, VISITED);
		while ((neighbor = grid.randomNeighbor(cell, c -> grid.get(c) == UNVISITED)) != null) {
			grid.addEdge(new DefaultEdge<>(cell, neighbor));
			accept(neighbor);
		}
		grid.set(cell, COMPLETED);
	}
}
