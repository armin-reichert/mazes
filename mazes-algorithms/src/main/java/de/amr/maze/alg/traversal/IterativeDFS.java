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
		Deque<Integer> frontier = new ArrayDeque<>();
		int current = grid.cell(x, y);
		grid.set(current, VISITED);
		frontier.push(current);
		while (!frontier.isEmpty()) {
			OptionalInt unvisitedNeighbor = randomUnvisitedNeighbor(current);
			if (unvisitedNeighbor.isPresent()) {
				int neighbor = unvisitedNeighbor.getAsInt();
				grid.addEdge(current, neighbor);
				grid.set(neighbor, VISITED);
				if (randomUnvisitedNeighbor(neighbor).isPresent()) {
					frontier.push(neighbor);
				}
				current = neighbor;
			}
			else {
				grid.set(current, COMPLETED);
				if (!frontier.isEmpty()) {
					current = frontier.peek();
					if (isCellCompleted(current)) {
						frontier.pop();
					}
				}
			}
		}
	}

	private OptionalInt randomUnvisitedNeighbor(int cell) {
		return randomElement(grid.neighbors(cell).filter(this::isCellUnvisited));
	}
}