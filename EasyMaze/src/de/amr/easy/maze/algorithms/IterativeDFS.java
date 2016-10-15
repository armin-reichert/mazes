package de.amr.easy.maze.algorithms;

import static de.amr.easy.graph.api.TraversalState.COMPLETED;
import static de.amr.easy.graph.api.TraversalState.UNVISITED;
import static de.amr.easy.graph.api.TraversalState.VISITED;

import java.util.Deque;
import java.util.LinkedList;
import java.util.Optional;

import de.amr.easy.graph.api.TraversalState;
import de.amr.easy.grid.api.ObservableDataGrid2D;

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

	private final Deque<Integer> stack = new LinkedList<>();

	public IterativeDFS(ObservableDataGrid2D<TraversalState> grid) {
		super(grid);
	}

	@Override
	public void accept(Integer start) {
		Integer current = start;
		stack.push(current);
		grid.set(current, VISITED);
		while (!stack.isEmpty()) {
			Optional<Integer> neighbor = grid.randomNeighbor(current).filter(cell -> grid.get(cell) == UNVISITED);
			if (neighbor.isPresent()) {
				if (grid.randomNeighbor(neighbor.get()).isPresent()) {
					stack.push(neighbor.get());
				}
				grid.addEdge(current, neighbor.get());
				grid.set(neighbor.get(), VISITED);
				current = neighbor.get();
			} else {
				grid.set(current, COMPLETED);
				if (!stack.isEmpty()) {
					current = stack.pop();
				}
				if (grid.get(current) != COMPLETED) {
					stack.push(current);
				}
			}
		}
	}
}