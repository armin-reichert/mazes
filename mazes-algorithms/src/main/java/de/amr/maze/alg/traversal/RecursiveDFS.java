package de.amr.maze.alg.traversal;

import static de.amr.datastruct.StreamUtils.randomElement;
import static de.amr.graph.core.api.TraversalState.COMPLETED;
import static de.amr.graph.core.api.TraversalState.VISITED;

import java.util.OptionalInt;

import de.amr.graph.core.api.TraversalState;
import de.amr.graph.grid.api.GridGraph2D;
import de.amr.maze.alg.core.MazeGenerator;

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
public class RecursiveDFS extends MazeGenerator {

	public RecursiveDFS(GridGraph2D<TraversalState, Integer> grid) {
		super(grid);
	}

	@Override
	public void createMaze(int x, int y) {
		dfs(grid.cell(x, y));
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
		return randomElement(grid.neighbors(v).filter(this::isCellUnvisited));
	}
}