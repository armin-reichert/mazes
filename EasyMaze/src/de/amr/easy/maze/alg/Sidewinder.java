package de.amr.easy.maze.alg;

import static java.util.stream.IntStream.range;

import de.amr.easy.graph.api.traversal.TraversalState;
import de.amr.easy.grid.api.GridGraph2D;
import de.amr.easy.maze.alg.core.MazeAlgorithm;

/**
 * The "Sidewinder" algorithm.
 * 
 * @author Armin Reichert
 *
 * @see <a href=
 *      "http://weblog.jamisbuck.org/2011/2/3/maze-generation-sidewinder-algorithm.html">Jamis
 *      Buck's blog: Sidewinder algorithm</a>
 */
public class Sidewinder extends MazeAlgorithm<Void> {

	private int start;

	public Sidewinder(GridGraph2D<TraversalState, Void> grid) {
		super(grid);
	}

	@Override
	public void run(int ignore) {
		range(0, grid.numRows()).forEach(row -> {
			start = 0;
			range(0, grid.numCols()).forEach(col -> {
				if (row > 0 && (col == grid.numCols() - 1 || rnd.nextBoolean())) {
					int passageCol = start + rnd.nextInt(col - start + 1);
					int north = grid.cell(passageCol, row - 1), south = grid.cell(passageCol, row);
					addTreeEdge(north, south);
					start = col + 1;
				} else if (col + 1 < grid.numCols()) {
					int west = grid.cell(col, row), east = grid.cell(col + 1, row);
					addTreeEdge(west, east);
				}
			});
		});
	}
}
