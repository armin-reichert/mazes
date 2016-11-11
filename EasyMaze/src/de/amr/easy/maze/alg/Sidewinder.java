package de.amr.easy.maze.alg;

import static de.amr.easy.graph.api.TraversalState.COMPLETED;
import static java.util.stream.IntStream.range;

import de.amr.easy.graph.api.TraversalState;
import de.amr.easy.grid.api.Grid2D;
import de.amr.easy.grid.api.dir.Dir4;

/**
 * The "Sidewinder" algorithm.
 * 
 * @author Armin Reichert
 *
 * @see <a href=
 *      "http://weblog.jamisbuck.org/2011/2/3/maze-generation-sidewinder-algorithm.html">Jamis
 *      Buck's blog: Sidewinder algorithm</a>
 */
public class Sidewinder extends MazeAlgorithm {

	private Integer runStart;

	public Sidewinder(Grid2D<Dir4,TraversalState, Integer> grid) {
		super(grid);
	}

	@Override
	public void accept(Integer start) {
		range(0, grid.numRows()).forEach(row -> {
			runStart = 0;
			range(0, grid.numCols()).forEach(col -> {
				if (row > 0 && (col == grid.numCols() - 1 || rnd.nextBoolean())) {
					Integer passageCol = runStart + rnd.nextInt(col - runStart + 1);
					Integer north = grid.cell(passageCol, row - 1), south = grid.cell(passageCol, row);
					grid.addEdge(north, south);
					grid.set(north, COMPLETED);
					grid.set(south, COMPLETED);
					runStart = col + 1;
				} else if (col + 1 < grid.numCols()) {
					Integer west = grid.cell(col, row), east = grid.cell(col + 1, row);
					grid.addEdge(west, east);
					grid.set(west, COMPLETED);
					grid.set(east, COMPLETED);
				}
			});
		});
	}
}
