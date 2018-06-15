package de.amr.easy.maze.alg.traversal;

import static de.amr.easy.graph.api.TraversalState.COMPLETED;
import static de.amr.easy.graph.api.TraversalState.VISITED;
import static de.amr.easy.util.StreamUtils.randomElement;

import java.util.OptionalInt;

import de.amr.easy.data.Stack;
import de.amr.easy.graph.api.SimpleEdge;
import de.amr.easy.graph.api.TraversalState;
import de.amr.easy.grid.api.Grid2D;
import de.amr.easy.maze.alg.MazeAlgorithm;

/**
 * Generates a maze by iterative random depth-first-traversal of a grid.
 * 
 * @author Armin Reichert
 */
public class IterativeDFS extends MazeAlgorithm<SimpleEdge> {

	public IterativeDFS(Grid2D<TraversalState, SimpleEdge> grid) {
		super(grid);
	}

	@Override
	public void run(int start) {
		Stack<Integer> stack = new Stack<>();
		int current = start;
		grid.set(current, VISITED);
		stack.push(current);
		while (!stack.isEmpty()) {
			OptionalInt unvisitedNeighbor = randomUnvisitedNeighbor(current);
			if (unvisitedNeighbor.isPresent()) {
				int neighbor = unvisitedNeighbor.getAsInt();
				grid.addEdge(current, neighbor);
				grid.set(neighbor, VISITED);
				if (randomUnvisitedNeighbor(neighbor).isPresent()) {
					stack.push(neighbor);
				}
				current = neighbor;
			} else {
				grid.set(current, COMPLETED);
				if (!stack.isEmpty()) {
					current = stack.pop();
				}
				if (!isCellCompleted.test(current)) {
					stack.push(current);
				}
			}
		}
	}

	private OptionalInt randomUnvisitedNeighbor(int cell) {
		return randomElement(grid.neighbors(cell).filter(isCellUnvisited));
	}
}