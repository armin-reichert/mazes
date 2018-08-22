package de.amr.easy.maze.alg.traversal;

import static de.amr.easy.graph.api.traversal.TraversalState.COMPLETED;
import static de.amr.easy.graph.api.traversal.TraversalState.UNVISITED;
import static de.amr.easy.graph.api.traversal.TraversalState.VISITED;
import static de.amr.easy.grid.impl.OrthogonalGrid.emptyGrid;
import static de.amr.easy.util.StreamUtils.randomElement;

import java.util.OptionalInt;

import de.amr.easy.grid.impl.OrthogonalGrid;
import de.amr.easy.maze.alg.core.MazeGenerator;

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
		randomDFS(grid.cell(x, y));
		return grid;
	}

	private void randomDFS(int v) {
		grid.set(v, VISITED);
		for (OptionalInt nb = randomUnvisitedNeighbor(v); nb.isPresent(); nb = randomUnvisitedNeighbor(v)) {
			int neighbor = nb.getAsInt();
			grid.addEdge(v, neighbor);
			randomDFS(neighbor);
		}
		grid.set(v, COMPLETED);
	}

	private OptionalInt randomUnvisitedNeighbor(int v) {
		return randomElement(grid.neighbors(v).filter(grid::isUnvisited));
	}
}