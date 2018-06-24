package de.amr.easy.maze.alg.traversal;

import static de.amr.easy.graph.api.traversal.TraversalState.COMPLETED;
import static de.amr.easy.graph.api.traversal.TraversalState.UNVISITED;
import static de.amr.easy.graph.api.traversal.TraversalState.VISITED;
import static de.amr.easy.util.StreamUtils.permute;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import de.amr.easy.maze.alg.core.ObservableMazeGenerator;
import de.amr.easy.maze.alg.core.OrthogonalGrid;

/**
 * Maze generator using a randomized breadth-first-traversal.
 * 
 * @author Armin Reichert
 */
public class RandomBFS extends ObservableMazeGenerator {

	private final Random rnd = new Random();

	public RandomBFS(int numCols, int numRows) {
		super(numCols, numRows, false, UNVISITED);
	}

	@Override
	public OrthogonalGrid createMaze(int x, int y) {
		List<Integer> frontier = new ArrayList<>();
		int start = maze.cell(x, y);
		maze.set(start, VISITED);
		frontier.add(start);
		while (!frontier.isEmpty()) {
			int cell = frontier.remove(rnd.nextInt(frontier.size()));
			permute(maze.neighbors(cell)).filter(maze::isUnvisited).forEach(neighbor -> {
				maze.addEdge(cell, neighbor);
				maze.set(neighbor, VISITED);
				frontier.add(neighbor);
			});
			maze.set(cell, COMPLETED);
		}
		return maze;
	}
}