package de.amr.easy.maze.alg.traversal;

import static de.amr.easy.graph.api.traversal.TraversalState.COMPLETED;
import static de.amr.easy.graph.api.traversal.TraversalState.UNVISITED;
import static de.amr.easy.graph.api.traversal.TraversalState.VISITED;
import static de.amr.easy.util.StreamUtils.randomElement;

import java.util.OptionalInt;

import de.amr.easy.maze.alg.core.ObservableMazeGenerator;
import de.amr.easy.maze.alg.core.OrthogonalGrid;

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
public class RecursiveDFS extends ObservableMazeGenerator {

	public RecursiveDFS(int numCols, int numRows) {
		super(numCols, numRows, false, UNVISITED);
	}

	@Override
	public OrthogonalGrid createMaze(int x, int y) {
		dfs(maze.cell(x, y));
		return maze;
	}

	private void dfs(int v) {
		maze.set(v, VISITED);
		for (OptionalInt optNeighbor = neighbor(v); optNeighbor.isPresent(); optNeighbor = neighbor(v)) {
			int neighbor = optNeighbor.getAsInt();
			maze.addEdge(v, neighbor);
			dfs(neighbor);
		}
		maze.set(v, COMPLETED);
	}

	private OptionalInt neighbor(int v) {
		return randomElement(maze.neighbors(v).filter(maze::isUnvisited));
	}
}