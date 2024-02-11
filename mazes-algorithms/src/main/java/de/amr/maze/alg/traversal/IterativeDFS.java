package de.amr.maze.alg.traversal;

import static de.amr.datastruct.StreamUtils.randomElement;
import static de.amr.graph.core.api.TraversalState.COMPLETED;
import static de.amr.graph.core.api.TraversalState.VISITED;

import java.util.ArrayDeque;
import java.util.Deque;

import de.amr.graph.core.api.Graph;
import de.amr.graph.core.api.TraversalState;
import de.amr.graph.grid.api.GridGraph2D;
import de.amr.maze.alg.core.MazeGenerator;

/**
 * Generates a maze by iterative random depth-first traversal of a grid.
 * 
 * @author Armin Reichert
 */
public class IterativeDFS extends MazeGenerator {

	public IterativeDFS(GridGraph2D<TraversalState, Integer> grid) {
		super(grid);
	}

	@Override
	public void createMaze(int x, int y) {
		Deque<Integer> stack = new ArrayDeque<>();
		int current = grid.cell(x, y);
		grid.set(current, VISITED);
		stack.push(current);
		while (!stack.isEmpty()) {
			int neighbor = randomElement(grid.neighbors(current).filter(this::isCellUnvisited)).orElse(Graph.NO_VERTEX);
			if (neighbor != Graph.NO_VERTEX) {
				grid.addEdge(current, neighbor);
				grid.set(neighbor, VISITED);
				stack.push(neighbor);
				current = neighbor;
			}
			else {
				grid.set(current, COMPLETED);
				// Note: current = stack.pop() would also be correct. The following lines
				// just give a better visualization.
				current = stack.peek();
				if (isCellCompleted(current)) {
					stack.pop();
				}
			}
		}
	}
}