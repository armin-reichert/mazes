package de.amr.easy.maze.alg;

import static de.amr.easy.graph.api.TraversalState.COMPLETED;
import static de.amr.easy.graph.api.TraversalState.VISITED;

import java.util.OptionalInt;

import de.amr.easy.graph.api.TraversalState;
import de.amr.easy.grid.api.Grid2D;

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
public class RecursiveDFS extends MazeAlgorithm {

	public RecursiveDFS(Grid2D<TraversalState, Integer> grid) {
		super(grid);
	}

	@Override
	public void run(int cell) {
		grid.set(cell, VISITED);
		for (OptionalInt neighbor = randomNeighbor(cell); neighbor.isPresent(); neighbor = randomNeighbor(cell)) {
			grid.addEdge(cell, neighbor.getAsInt());
			run(neighbor.getAsInt());
		}
		grid.set(cell, COMPLETED);
	}

	private OptionalInt randomNeighbor(int cell) {
		return grid.neighborsPermuted(cell).filter(this::isCellUnvisited).findAny();
	}
}