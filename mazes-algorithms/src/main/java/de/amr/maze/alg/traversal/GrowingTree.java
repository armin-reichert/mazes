package de.amr.maze.alg.traversal;

import static de.amr.datastruct.StreamUtils.permute;
import static de.amr.graph.core.api.TraversalState.COMPLETED;
import static de.amr.graph.core.api.TraversalState.UNVISITED;
import static de.amr.graph.core.api.TraversalState.VISITED;

import java.util.ArrayList;
import java.util.List;

import de.amr.graph.core.api.TraversalState;
import de.amr.graph.grid.api.GridGraph2D;
import de.amr.maze.alg.core.MazeGridFactory;
import de.amr.maze.alg.core.MazeGenerator;

/**
 * "Growing tree" base algorithm.
 * 
 * @see <a href=
 *      "http://weblog.jamisbuck.org/2011/1/27/maze-generation-growing-tree-algorithm.html">Maze
 *      Generation: Growing Tree algorithm</a>
 * 
 * @author Armin Reichert
 */
public abstract class GrowingTree implements MazeGenerator {

	private final GridGraph2D<TraversalState, Integer> grid;

	public GrowingTree(MazeGridFactory factory, int numCols, int numRows) {
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
			int cell = selectCell(frontier);
			permute(grid.neighbors(cell).filter(this::isUnvisited)).forEach(neighbor -> {
				grid.set(neighbor, VISITED);
				frontier.add(neighbor);
				grid.addEdge(cell, neighbor);
			});
			grid.set(cell, COMPLETED);
		}
		return grid;
	}

	/** Selects and removes a cell from the frontier. */
	protected abstract int selectCell(List<Integer> frontier);
}