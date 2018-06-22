package de.amr.easy.maze.alg.traversal;

import static de.amr.easy.graph.api.traversal.TraversalState.COMPLETED;
import static de.amr.easy.graph.api.traversal.TraversalState.VISITED;
import static de.amr.easy.util.StreamUtils.permute;

import java.util.ArrayList;
import java.util.List;

import de.amr.easy.graph.api.traversal.TraversalState;
import de.amr.easy.grid.api.GridGraph2D;
import de.amr.easy.maze.alg.core.MazeAlgorithm;

/**
 * Maze generator using a randomized breadth-first-traversal.
 * 
 * @author Armin Reichert
 */
public class RandomBFS extends MazeAlgorithm<Void> {

	public RandomBFS(GridGraph2D<TraversalState, Void> grid) {
		super(grid);
	}

	@Override
	public void run(int start) {
		List<Integer> frontier = new ArrayList<>();
		grid.set(start, VISITED);
		frontier.add(start);
		while (!frontier.isEmpty()) {
			int cell = frontier.remove(rnd.nextInt(frontier.size()));
			permute(grid.neighbors(cell)).filter(isCellUnvisited).forEach(neighbor -> {
				grid.addEdge(cell, neighbor);
				grid.set(neighbor, VISITED);
				frontier.add(neighbor);
			});
			grid.set(cell, COMPLETED);
		}
	}
}