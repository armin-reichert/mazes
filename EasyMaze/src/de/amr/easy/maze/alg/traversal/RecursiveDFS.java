package de.amr.easy.maze.alg.traversal;

import static de.amr.easy.graph.api.TraversalState.COMPLETED;
import static de.amr.easy.graph.api.TraversalState.VISITED;
import static de.amr.easy.util.StreamUtils.randomElement;

import java.util.OptionalInt;

import de.amr.easy.graph.api.SimpleEdge;
import de.amr.easy.graph.api.TraversalState;
import de.amr.easy.grid.api.Grid2D;
import de.amr.easy.maze.alg.MazeAlgorithm;

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
public class RecursiveDFS extends MazeAlgorithm<SimpleEdge> {

	public RecursiveDFS(Grid2D<TraversalState, SimpleEdge> grid) {
		super(grid);
	}

	@Override
	public void run(int cell) {
		grid.set(cell, VISITED);
		for (OptionalInt neighbor = unvisitedNeighbor(cell); neighbor.isPresent(); neighbor = unvisitedNeighbor(cell)) {
			grid.addEdge(cell, neighbor.getAsInt());
			run(neighbor.getAsInt());
		}
		grid.set(cell, COMPLETED);
	}

	private OptionalInt unvisitedNeighbor(int cell) {
		return randomElement(grid.neighbors(cell).filter(isCellUnvisited));
	}
}