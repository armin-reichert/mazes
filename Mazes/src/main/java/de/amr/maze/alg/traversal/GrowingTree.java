package de.amr.maze.alg.traversal;

import static de.amr.datastruct.StreamUtils.permute;
import static de.amr.graph.grid.impl.OrthogonalGrid.emptyGrid;
import static de.amr.graph.pathfinder.api.TraversalState.COMPLETED;
import static de.amr.graph.pathfinder.api.TraversalState.UNVISITED;
import static de.amr.graph.pathfinder.api.TraversalState.VISITED;

import java.util.ArrayList;
import java.util.List;

import de.amr.graph.grid.impl.OrthogonalGrid;
import de.amr.maze.alg.core.MazeGenerator;

/**
 * Maze generator using a randomized expansion of a frontier.
 * 
 * @author Armin Reichert
 */
public abstract class GrowingTree implements MazeGenerator<OrthogonalGrid> {

	private final OrthogonalGrid grid;

	public GrowingTree(int numCols, int numRows) {
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
		frontier.add(start);
		grid.set(start, VISITED);
		while (!frontier.isEmpty()) {
			int cell = selectCell(frontier);
			permute(grid.neighbors(cell).filter(grid::isUnvisited)).forEach(neighbor -> {
				grid.addEdge(cell, neighbor);
				grid.set(neighbor, VISITED);
				frontier.add(neighbor);
			});
			grid.set(cell, COMPLETED);
		}
		return grid;
	}

	/** Selects and removes a cell from the frontier. */
	protected abstract int selectCell(List<Integer> frontier);
}