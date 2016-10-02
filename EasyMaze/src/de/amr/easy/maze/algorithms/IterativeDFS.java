package de.amr.easy.maze.algorithms;

import static de.amr.easy.graph.api.TraversalState.COMPLETED;
import static de.amr.easy.graph.api.TraversalState.UNVISITED;
import static de.amr.easy.graph.api.TraversalState.VISITED;

import java.util.Deque;
import java.util.LinkedList;
import java.util.function.Consumer;

import de.amr.easy.graph.api.TraversalState;
import de.amr.easy.graph.impl.DefaultEdge;
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
 * 
 * @param <Integer>
 *          grid cell type
 */
public class IterativeDFS implements Consumer<Integer> {

	private final ObservableDataGrid2D<Integer, DefaultEdge<Integer>, TraversalState> grid;
	private final Deque<Integer> stack;

	public IterativeDFS(ObservableDataGrid2D<Integer, DefaultEdge<Integer>, TraversalState> grid) {
		this.grid = grid;
		stack = new LinkedList<>();
	}

	@Override
	public void accept(Integer start) {
		Integer current = start;
		stack.push(current);
		grid.set(current, VISITED);
		while (!stack.isEmpty()) {
			Integer neighbor = grid.randomNeighbor(current, cell -> grid.get(cell) == UNVISITED);
			if (neighbor != null) {
				if (grid.randomNeighbor(neighbor, cell -> true) != null) {
					stack.push(neighbor);
				}
				grid.addEdge(new DefaultEdge<>(current, neighbor));
				grid.set(neighbor, VISITED);
				current = neighbor;
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
