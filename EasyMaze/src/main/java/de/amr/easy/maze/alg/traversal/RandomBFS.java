package de.amr.easy.maze.alg.traversal;

import static de.amr.datastruct.StreamUtils.permute;
import static de.amr.graph.grid.impl.OrthogonalGrid.emptyGrid;
import static de.amr.graph.pathfinder.api.TraversalState.COMPLETED;
import static de.amr.graph.pathfinder.api.TraversalState.UNVISITED;
import static de.amr.graph.pathfinder.api.TraversalState.VISITED;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import de.amr.easy.maze.alg.core.MazeGenerator;
import de.amr.graph.grid.impl.OrthogonalGrid;

/**
 * Maze generator using a randomized breadth-first-traversal.
 * 
 * @author Armin Reichert
 */
public class RandomBFS implements MazeGenerator<OrthogonalGrid> {

	private OrthogonalGrid grid;
	private Random rnd = new Random();

	public RandomBFS(int numCols, int numRows) {
		grid = emptyGrid(numCols, numRows, UNVISITED);
	}

	@Override
	public OrthogonalGrid getGrid() {
		return grid;
	}

	@Override
	public OrthogonalGrid createMaze(int x, int y) {
		List<Integer> frontier = new ArrayList<>();
		int start = grid.cell(x, y);
		grid.set(start, VISITED);
		frontier.add(start);
		while (!frontier.isEmpty()) {
			int cell = frontier.remove(rnd.nextInt(frontier.size()));
			grid.set(cell, COMPLETED);
			permute(grid.neighbors(cell).filter(grid::isUnvisited)).forEach(neighbor -> {
				grid.addEdge(cell, neighbor);
				grid.set(neighbor, VISITED);
				frontier.add(neighbor);
			});
		}
		return grid;
	}
}