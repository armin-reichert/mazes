package de.amr.maze.alg.others;

import static de.amr.graph.core.api.TraversalState.COMPLETED;
import static java.util.stream.IntStream.range;

import de.amr.graph.core.api.TraversalState;
import de.amr.graph.grid.api.GridGraph2D;
import de.amr.maze.alg.core.MazeGenerator;

/**
 * Creates maze by recursive division.
 * 
 * @author Armin Reichert
 * 
 * @see <a href= "http://weblog.jamisbuck.org/2011/1/12/maze-generation-recursive-division-algorithm.html">Maze
 *      Generation: Recursive Division</a>
 */
public class RecursiveDivision extends MazeGenerator {

	public RecursiveDivision(GridGraph2D<TraversalState, Integer> grid) {
		super(grid);
		grid.fillOrthogonal();
		grid.setDefaultVertexLabel(cell -> COMPLETED);
	}

	@Override
	public void createMaze(int x, int y) {
		divide(0, 0, grid.numCols(), grid.numRows());
	}

	/**
	 * Divides the {@code (w x h)}-subgrid at top-left position {@code (x0, y0)}.
	 * 
	 * @param x       top-left x-position
	 * @param y       top-left y-position
	 * @param numCols subgrid width
	 * @param numRows subgrid height
	 */
	private void divide(int x, int y, int numCols, int numRows) {
		if (numCols <= 1 && numRows <= 1) {
			return;
		}
		if (numCols < numRows || (numCols == numRows && rnd.nextBoolean())) {
			// Build "horizontal wall" at random row in range [y + 1, y + numRows - 1), keep random door
			int row = y + 1 + rnd.nextInt(numRows - 1);
			int door = x + rnd.nextInt(numCols);
			range(x, x + numCols).filter(col -> col != door)
					.forEach(col -> grid.edge(grid.cell(col, row - 1), grid.cell(col, row)).ifPresent(grid::removeEdge));
			divide(x, y, numCols, row - y);
			divide(x, row, numCols, numRows - (row - y));
		} else {
			// Build "vertical wall" at random col in range [x + 1, x + numCols - 1), keep random door
			int col = x + 1 + rnd.nextInt(numCols - 1);
			int door = y + rnd.nextInt(numRows);
			range(y, y + numRows).filter(row -> row != door)
					.forEach(row -> grid.edge(grid.cell(col - 1, row), grid.cell(col, row)).ifPresent(grid::removeEdge));
			divide(x, y, col - x, numRows);
			divide(col, y, numCols - (col - x), numRows);
		}
	}
}