package de.amr.maze.alg.traversal;

import static de.amr.datastruct.StreamUtils.randomElement;
import static de.amr.graph.core.api.TraversalState.COMPLETED;
import static de.amr.graph.core.api.TraversalState.VISITED;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.OptionalInt;

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
			OptionalInt unvisitedNeighbor = randomUnvisitedNeighbor(current);
			if (unvisitedNeighbor.isPresent()) {
				int neighbor = unvisitedNeighbor.getAsInt();
				grid.addEdge(current, neighbor);
				grid.set(neighbor, VISITED);
				if (randomUnvisitedNeighbor(neighbor).isPresent()) {
					stack.push(neighbor);
				}
				current = neighbor;
			}
			else {
				grid.set(current, COMPLETED);
				if (!stack.isEmpty()) {
					current = stack.pop();
				}
				if (!isCellCompleted(current)) {
					stack.push(current);
				}
			}
		}
	}

	private OptionalInt randomUnvisitedNeighbor(int cell) {
		return randomElement(grid.neighbors(cell).filter(this::isCellUnvisited));
	}
}