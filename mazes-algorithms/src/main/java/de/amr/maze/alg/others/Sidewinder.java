package de.amr.maze.alg.others;

import static de.amr.graph.core.api.TraversalState.COMPLETED;

import de.amr.graph.core.api.TraversalState;
import de.amr.graph.grid.api.GridGraph2D;
import de.amr.maze.alg.core.MazeGenerator;

/**
 * The "Sidewinder" algorithm.
 * 
 * @author Armin Reichert
 *
 * @see <a href= "http://weblog.jamisbuck.org/2011/2/3/maze-generation-sidewinder-algorithm.html">Jamis Buck's blog:
 *      Sidewinder algorithm</a>
 */
public class Sidewinder extends MazeGenerator {

	public Sidewinder(GridGraph2D<TraversalState, Integer> grid) {
		super(grid);
	}

	@Override
	public void createMaze(int x, int y) {
		for (int row = 0; row < grid.numRows(); ++row) {
			int current = 0;
			for (int col = 0; col < grid.numCols(); ++col) {
				if (row > 0 && (col == grid.numCols() - 1 || rnd.nextBoolean())) {
					int passageCol = current + rnd.nextInt(col - current + 1);
					int north = grid.cell(passageCol, row - 1);
					int south = grid.cell(passageCol, row);
					grid.addEdge(north, south);
					grid.set(north, COMPLETED);
					grid.set(south, COMPLETED);
					current = col + 1;
				} else if (col + 1 < grid.numCols()) {
					int west = grid.cell(col, row);
					int east = grid.cell(col + 1, row);
					grid.addEdge(west, east);
					grid.set(west, COMPLETED);
					grid.set(east, COMPLETED);
				}
			}
		}
	}
}