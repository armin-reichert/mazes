package de.amr.easy.maze.alg;

import static de.amr.easy.graph.api.TraversalState.COMPLETED;
import static de.amr.easy.graph.api.TraversalState.VISITED;

import java.util.ArrayList;
import java.util.List;

import de.amr.easy.graph.api.TraversalState;
import de.amr.easy.grid.api.Grid2D;

/**
 * Maze generator using a randomized breadth-first-traversal.
 * 
 * @author Armin Reichert
 */
public class RandomBFS extends MazeAlgorithm {

	private final List<Integer> frontier = new ArrayList<>();

	public RandomBFS(Grid2D<TraversalState, Integer> grid) {
		super(grid);
	}

	@Override
	public void run(int start) {
		extendMaze(start);
		while (!frontier.isEmpty()) {
			Integer cell = frontier.remove(rnd.nextInt(frontier.size()));
			grid.neighborsPermuted(cell).filter(this::isCellUnvisited).forEach(neighbor -> {
				extendMaze(neighbor);
				grid.addEdge(cell, neighbor);
			});
			grid.set(cell, COMPLETED);
		}
	}

	private void extendMaze(int cell) {
		grid.set(cell, VISITED);
		frontier.add(cell);
	}
}