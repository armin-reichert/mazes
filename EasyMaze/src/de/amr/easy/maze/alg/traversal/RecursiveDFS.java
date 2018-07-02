package de.amr.easy.maze.alg.traversal;

import static de.amr.easy.graph.api.traversal.TraversalState.COMPLETED;
import static de.amr.easy.graph.api.traversal.TraversalState.UNVISITED;
import static de.amr.easy.graph.api.traversal.TraversalState.VISITED;
import static de.amr.easy.util.StreamUtils.randomElement;

import java.util.OptionalInt;

import de.amr.easy.maze.alg.core.OrthogonalGrid;
import de.amr.easy.maze.alg.core.OrthogonalMazeGenerator;

/**
 * Maze generator using randomized recursive depth-first-search. Not suited for larger grids because
 * of stack overflow.
 * 
 * @author Armin Reichert
 * 
 * @see <a href=
 *      "http://weblog.jamisbuck.org/2010/12/27/maze-generation-recursive-backtracking">Maze
 *      Generation: Recursive Backtracking</a>
 */
public class RecursiveDFS implements OrthogonalMazeGenerator {

	private OrthogonalGrid grid;

	public RecursiveDFS(int numCols, int numRows) {
		grid = OrthogonalGrid.emptyGrid(numCols, numRows, UNVISITED);
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
		for (OptionalInt optNeighbor = neighbor(v); optNeighbor.isPresent(); optNeighbor = neighbor(v)) {
			int neighbor = optNeighbor.getAsInt();
			grid.addEdge(v, neighbor);
			dfs(neighbor);
		}
		grid.set(v, COMPLETED);
	}

	private OptionalInt neighbor(int v) {
		return randomElement(grid.neighbors(v).filter(grid::isUnvisited));
	}
}