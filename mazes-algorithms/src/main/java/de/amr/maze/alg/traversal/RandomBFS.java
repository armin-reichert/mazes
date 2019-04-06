package de.amr.maze.alg.traversal;

import static de.amr.datastruct.StreamUtils.permute;
import static de.amr.graph.core.api.TraversalState.COMPLETED;
import static de.amr.graph.core.api.TraversalState.UNVISITED;
import static de.amr.graph.core.api.TraversalState.VISITED;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import de.amr.graph.core.api.TraversalState;
import de.amr.graph.grid.api.GridGraph2D;
import de.amr.maze.alg.core.MazeGridFactory;
import de.amr.maze.alg.core.MazeGenerator;

/**
 * Maze generator using a randomized breadth-first-traversal.
 * 
 * @author Armin Reichert
 */
public class RandomBFS implements MazeGenerator {

	private GridGraph2D<TraversalState, Integer> grid;
	private Random rnd = new Random();

	public RandomBFS(MazeGridFactory factory, int numCols, int numRows) {
		grid = factory.emptyGrid(numCols, numRows, UNVISITED);
	}

	@Override
	public GridGraph2D<TraversalState, Integer> getGrid() {
		return grid;
	}

	@Override
	public GridGraph2D<TraversalState, Integer> createMaze(int x, int y) {
		List<Integer> frontier = new ArrayList<>();
		int start = grid.cell(x, y);
		grid.set(start, VISITED);
		frontier.add(start);
		while (!frontier.isEmpty()) {
			int cell = frontier.remove(rnd.nextInt(frontier.size()));
			grid.set(cell, COMPLETED);
			permute(grid.neighbors(cell).filter(this::isUnvisited)).forEach(neighbor -> {
				grid.addEdge(cell, neighbor);
				grid.set(neighbor, VISITED);
				frontier.add(neighbor);
			});
		}
		return grid;
	}
}