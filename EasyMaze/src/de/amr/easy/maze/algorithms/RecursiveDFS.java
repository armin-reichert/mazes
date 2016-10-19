package de.amr.easy.maze.algorithms;

import static de.amr.easy.graph.api.TraversalState.COMPLETED;
import static de.amr.easy.graph.api.TraversalState.UNVISITED;
import static de.amr.easy.graph.api.TraversalState.VISITED;

import java.util.Optional;
import java.util.function.Predicate;

import de.amr.easy.graph.api.TraversalState;
import de.amr.easy.grid.api.DataGrid2D;

/**
 * Maze generator using randomized recursive depth-first-search. Not suited for larger grids because
 * of stack overflow.
 * 
 * @author Armin Reichert
 */
public class RecursiveDFS extends MazeAlgorithm {

	private Optional<Integer> unvisitedNeighbor;
	private final Predicate<Integer> isUnvisited = cell -> grid.get(cell) == UNVISITED;

	public RecursiveDFS(DataGrid2D<TraversalState> grid) {
		super(grid);
	}

	@Override
	public void accept(Integer cell) {
		grid.set(cell, VISITED);
		while ((unvisitedNeighbor = grid.neighborsPermuted(cell).filter(isUnvisited).findAny()).isPresent()) {
			grid.addEdge(cell, unvisitedNeighbor.get());
			accept(unvisitedNeighbor.get());
		}
		grid.set(cell, COMPLETED);
	}
}