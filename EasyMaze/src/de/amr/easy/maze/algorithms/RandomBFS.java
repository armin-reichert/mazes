package de.amr.easy.maze.algorithms;

import static de.amr.easy.graph.api.TraversalState.COMPLETED;
import static de.amr.easy.graph.api.TraversalState.VISITED;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import de.amr.easy.graph.api.TraversalState;
import de.amr.easy.grid.api.ObservableDataGrid2D;

/**
 * Maze generator using a randomized breadth-first-traversal.
 * 
 * @author Armin Reichert
 */
public class RandomBFS extends MazeAlgorithm {

	private final Set<Integer> maze = new HashSet<>();
	private final List<Integer> frontier = new LinkedList<>();

	public RandomBFS(ObservableDataGrid2D<TraversalState> grid) {
		super(grid);
	}

	@Override
	public void accept(Integer start) {
		extendMaze(start);
		while (!frontier.isEmpty()) {
			Integer cell = frontier.remove(rnd.nextInt(frontier.size()));
			/*@formatter:off*/
			grid.neighbors(cell)
				.filter(neighbor -> !maze.contains(neighbor))
				.forEach(neighbor -> {
					extendMaze(neighbor);
					grid.addEdge(cell, neighbor);
				});
			/*@formatter:on*/
			grid.set(cell, COMPLETED);
		}
	}

	private void extendMaze(Integer cell) {
		grid.set(cell, VISITED);
		maze.add(cell);
		frontier.add(cell);
	}
}