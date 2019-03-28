package de.amr.maze.alg.traversal;

import static de.amr.datastruct.StreamUtils.randomElement;
import static de.amr.graph.core.api.TraversalState.COMPLETED;
import static de.amr.graph.core.api.TraversalState.UNVISITED;
import static de.amr.graph.core.api.TraversalState.VISITED;
import static de.amr.maze.alg.core.OrthogonalGrid.emptyGrid;

import java.util.OptionalInt;

import de.amr.maze.alg.core.MazeGenerator;
import de.amr.maze.alg.core.OrthogonalGrid;

/**
 * Maze generator using randomized, recursive depth-first search. Not suited for larger grids
 * because of stack overflow.
 * 
 * @author Armin Reichert
 * 
 * @see <a href=
 *      "http://weblog.jamisbuck.org/2010/12/27/maze-generation-recursive-backtracking">Maze
 *      Generation: Recursive Backtracking</a>
 */
public class RecursiveDFS implements MazeGenerator<OrthogonalGrid> {

	private OrthogonalGrid grid;

	public RecursiveDFS(int numCols, int numRows) {
		grid = emptyGrid(numCols, numRows, UNVISITED);
	}

	@Override
	public OrthogonalGrid getGrid() {
		return grid;
	}

	@Override
	public OrthogonalGrid createMaze(int x, int y) {
		dfs(grid.cell(x, y));
		return grid;
	}

	private void dfs(int v) {
		grid.set(v, VISITED);
		for (OptionalInt next = neighbor(v); next.isPresent(); next = neighbor(v)) {
			grid.addEdge(v, next.getAsInt());
			dfs(next.getAsInt());
		}
		grid.set(v, COMPLETED);
	}

	private OptionalInt neighbor(int v) {
		return randomElement(grid.neighbors(v).filter(grid::isUnvisited));
	}
}