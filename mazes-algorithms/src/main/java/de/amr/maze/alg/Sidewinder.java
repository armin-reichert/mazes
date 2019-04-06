package de.amr.maze.alg;

import static de.amr.graph.core.api.TraversalState.COMPLETED;
import static de.amr.graph.core.api.TraversalState.UNVISITED;
import static java.util.stream.IntStream.range;

import java.util.Random;

import de.amr.graph.core.api.TraversalState;
import de.amr.graph.grid.api.GridGraph2D;
import de.amr.maze.alg.core.MazeGridFactory;
import de.amr.maze.alg.core.MazeGenerator;

/**
 * The "Sidewinder" algorithm.
 * 
 * @author Armin Reichert
 *
 * @see <a href=
 *      "http://weblog.jamisbuck.org/2011/2/3/maze-generation-sidewinder-algorithm.html">Jamis
 *      Buck's blog: Sidewinder algorithm</a>
 */
public class Sidewinder implements MazeGenerator {

	private GridGraph2D<TraversalState, Integer> grid;
	private Random rnd = new Random();
	private int current;

	public Sidewinder(MazeGridFactory factory, int numCols, int numRows) {
		grid = factory.emptyGrid(numCols, numRows, UNVISITED);
	}

	@Override
	public GridGraph2D<TraversalState, Integer> getGrid() {
		return grid;
	}

	@Override
	public GridGraph2D<TraversalState, Integer> createMaze(int x, int y) {
		range(0, grid.numRows()).forEach(row -> {
			current = 0;
			range(0, grid.numCols()).forEach(col -> {
				if (row > 0 && (col == grid.numCols() - 1 || rnd.nextBoolean())) {
					int passageCol = current + rnd.nextInt(col - current + 1);
					int north = grid.cell(passageCol, row - 1), south = grid.cell(passageCol, row);
					grid.addEdge(north, south);
					grid.set(north, COMPLETED);
					grid.set(south, COMPLETED);
					current = col + 1;
				}
				else if (col + 1 < grid.numCols()) {
					int west = grid.cell(col, row), east = grid.cell(col + 1, row);
					grid.addEdge(west, east);
					grid.set(west, COMPLETED);
					grid.set(east, COMPLETED);
				}
			});
		});
		return grid;
	}
}