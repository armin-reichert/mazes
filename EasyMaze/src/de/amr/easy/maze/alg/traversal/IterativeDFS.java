package de.amr.easy.maze.alg.traversal;

import static de.amr.easy.graph.api.traversal.TraversalState.COMPLETED;
import static de.amr.easy.graph.api.traversal.TraversalState.UNVISITED;
import static de.amr.easy.graph.api.traversal.TraversalState.VISITED;
import static de.amr.easy.util.StreamUtils.randomElement;

import java.util.OptionalInt;

import de.amr.easy.data.Stack;
import de.amr.easy.maze.alg.core.ObservableMazeGenerator;
import de.amr.easy.maze.alg.core.OrthogonalGrid;

/**
 * Generates a maze by iterative random depth-first-traversal of a grid.
 * 
 * @author Armin Reichert
 */
public class IterativeDFS extends ObservableMazeGenerator {

	public IterativeDFS(int numCols, int numRows) {
		super(numCols, numRows, false, UNVISITED);
	}

	@Override
	public OrthogonalGrid createMaze(int x, int y) {
		Stack<Integer> stack = new Stack<>();
		int current = maze.cell(x, y);
		maze.set(current, VISITED);
		stack.push(current);
		while (!stack.isEmpty()) {
			OptionalInt unvisitedNeighbor = randomUnvisitedNeighbor(current);
			if (unvisitedNeighbor.isPresent()) {
				int neighbor = unvisitedNeighbor.getAsInt();
				maze.addEdge(current, neighbor);
				maze.set(neighbor, VISITED);
				if (randomUnvisitedNeighbor(neighbor).isPresent()) {
					stack.push(neighbor);
				}
				current = neighbor;
			} else {
				maze.set(current, COMPLETED);
				if (!stack.isEmpty()) {
					current = stack.pop();
				}
				if (!maze.isCompleted(current)) {
					stack.push(current);
				}
			}
		}
		return maze;
	}

	private OptionalInt randomUnvisitedNeighbor(int cell) {
		return randomElement(maze.neighbors(cell).filter(maze::isUnvisited));
	}
}