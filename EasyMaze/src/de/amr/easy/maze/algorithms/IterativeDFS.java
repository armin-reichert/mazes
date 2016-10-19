package de.amr.easy.maze.algorithms;

import static de.amr.easy.graph.api.TraversalState.COMPLETED;
import static de.amr.easy.graph.api.TraversalState.UNVISITED;
import static de.amr.easy.graph.api.TraversalState.VISITED;

import java.util.Deque;
import java.util.LinkedList;
import java.util.Optional;
import java.util.function.Predicate;

import de.amr.easy.graph.api.TraversalState;
import de.amr.easy.grid.api.DataGrid2D;

/**
 * Generates a maze by iterative random depth-first-traversal of a grid.
 * 
 * <p>
 * More information on maze creation by "Recursive backtracking" can be found
 * <a href= "http://weblog.jamisbuck.org/2010/12/27/maze-generation-recursive-backtracking"> here
 * </a>. The iterative DFS implementation is inspired by the
 * <a href="http://algs4.cs.princeton.edu/41graph/">online book</a> by Sedgewick and Wayne.
 * 
 * @author Armin Reichert
 */
public class IterativeDFS extends MazeAlgorithm {

	private final Predicate<Integer> isUnvisited = cell -> grid.get(cell) == UNVISITED;
	private final Deque<Integer> stack = new LinkedList<>();

	public IterativeDFS(DataGrid2D<TraversalState> grid) {
		super(grid);
	}

	@Override
	public void accept(Integer cell) {
		stack.push(cell);
		grid.set(cell, VISITED);
		while (!stack.isEmpty()) {
			Optional<Integer> unvisitedNeighbor = grid.neighborsPermuted(cell).filter(isUnvisited).findAny();
			if (unvisitedNeighbor.isPresent()) {
				Integer neighbor = unvisitedNeighbor.get();
				if (grid.randomNeighbor(neighbor).isPresent()) {
					stack.push(neighbor);
				}
				grid.addEdge(cell, neighbor);
				grid.set(neighbor, VISITED);
				cell = neighbor;
			} else {
				grid.set(cell, COMPLETED);
				if (!stack.isEmpty()) {
					cell = stack.pop();
				}
				if (grid.get(cell) != COMPLETED) {
					stack.push(cell);
				}
			}
		}
	}
}