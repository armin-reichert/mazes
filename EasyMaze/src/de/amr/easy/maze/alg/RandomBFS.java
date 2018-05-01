package de.amr.easy.maze.alg;

import static de.amr.easy.graph.api.TraversalState.COMPLETED;
import static de.amr.easy.graph.api.TraversalState.VISITED;
import static de.amr.easy.util.GridUtils.permute;

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

	public RandomBFS(Grid2D<TraversalState, Integer> grid) {
		super(grid);
	}

	@Override
	public void run(int start) {
		List<Integer> frontier = new ArrayList<>();
		grid.set(start, VISITED);
		frontier.add(start);
		while (!frontier.isEmpty()) {
			int cell = frontier.remove(rnd.nextInt(frontier.size()));
			permute(grid.neighbors(cell)).filter(this::isCellUnvisited).forEach(neighbor -> {
				grid.addEdge(cell, neighbor);
				grid.set(neighbor, VISITED);
				frontier.add(neighbor);
			});
			grid.set(cell, COMPLETED);
		}
	}
}