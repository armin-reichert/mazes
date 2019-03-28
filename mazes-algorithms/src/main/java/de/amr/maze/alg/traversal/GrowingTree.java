package de.amr.maze.alg.traversal;

import static de.amr.datastruct.StreamUtils.permute;
import static de.amr.graph.core.api.TraversalState.COMPLETED;
import static de.amr.graph.core.api.TraversalState.UNVISITED;
import static de.amr.graph.core.api.TraversalState.VISITED;
import static de.amr.maze.alg.core.OrthogonalGrid.emptyGrid;

import java.util.ArrayList;
import java.util.List;

import de.amr.maze.alg.core.MazeGenerator;
import de.amr.maze.alg.core.OrthogonalGrid;

/**
 * "Growing tree" base algorithm.
 * 
 * @see <a href=
 *      "http://weblog.jamisbuck.org/2011/1/27/maze-generation-growing-tree-algorithm.html">Maze
 *      Generation: Growing Tree algorithm</a>
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
		grid.set(start, VISITED);
		frontier.add(start);
		while (!frontier.isEmpty()) {
			int cell = selectCell(frontier);
			permute(grid.neighbors(cell).filter(grid::isUnvisited)).forEach(neighbor -> {
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