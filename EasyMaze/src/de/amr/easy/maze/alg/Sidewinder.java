package de.amr.easy.maze.alg;

import static de.amr.easy.graph.api.traversal.TraversalState.COMPLETED;
import static java.util.stream.IntStream.range;

import java.util.Random;

import de.amr.easy.maze.alg.core.MazeGenerator;
import de.amr.easy.maze.alg.core.OrthogonalGrid;

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

	private final OrthogonalGrid grid;
	private final Random rnd = new Random();
	private int start;

	public Sidewinder(OrthogonalGrid grid) {
		this.grid = grid;
	}

	@Override
	public void run(int ignore) {
		range(0, grid.numRows()).forEach(row -> {
			start = 0;
			range(0, grid.numCols()).forEach(col -> {
				if (row > 0 && (col == grid.numCols() - 1 || rnd.nextBoolean())) {
					int passageCol = start + rnd.nextInt(col - start + 1);
					int north = grid.cell(passageCol, row - 1), south = grid.cell(passageCol, row);
					grid.addEdge(north, south);
					grid.set(north, COMPLETED);
					grid.set(south, COMPLETED);
					start = col + 1;
				} else if (col + 1 < grid.numCols()) {
					int west = grid.cell(col, row), east = grid.cell(col + 1, row);
					grid.addEdge(west, east);
					grid.set(west, COMPLETED);
					grid.set(east, COMPLETED);
				}
			});
		});
	}
}
