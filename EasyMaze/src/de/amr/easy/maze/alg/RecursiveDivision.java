package de.amr.easy.maze.alg;

import static de.amr.easy.graph.api.traversal.TraversalState.COMPLETED;
import static java.lang.Math.min;
import static java.util.stream.IntStream.range;

import java.util.Random;

import de.amr.easy.maze.alg.core.MazeGenerator;
import de.amr.easy.maze.alg.core.OrthogonalGrid;

/**
 * Creates maze by recursive division.
 * 
 * @author Armin Reichert
 * 
 * @see <a href=
 *      "http://weblog.jamisbuck.org/2011/1/12/maze-generation-recursive-division-algorithm.html">Maze
 *      Generation: Recursive Division</a>
 */
public class RecursiveDivision implements MazeGenerator {

	private final OrthogonalGrid grid;
	private final Random rnd = new Random();

	public RecursiveDivision(OrthogonalGrid grid) {
		this.grid = grid;
	}

	@Override
	public void run(int start) {
		grid.fill();
		grid.setDefaultVertex(COMPLETED);
		divide(0, 0, grid.numCols(), grid.numRows());
	}

	/**
	 * Divides the {@code (w x h)}-subgrid with top-left position {@code (x0, y0)}.
	 * 
	 * @param x0
	 *          x-position of subgrid
	 * @param y0
	 *          y-position subgrid
	 * @param w
	 *          width of subgrid
	 * @param h
	 *          height of subgrid
	 */
	private void divide(int x0, int y0, int w, int h) {
		if (w <= 1 && h <= 1) {
			return;
		}
		if (w < h || (w == h && rnd.nextBoolean())) {
			// Build "horizontal wall" at random y from [y0 + 1, y0 + h - 1], keep random door
			int wy = min(y0 + 1 + rnd.nextInt(h - 1), grid.numRows() - 1);
			int door = rnd.nextInt(w);
			range(0, w).filter(x -> x != door).map(x -> x0 + x).forEach(x -> {
				grid.edge(grid.cell(x, wy - 1), grid.cell(x, wy)).ifPresent(grid::removeEdge);
			});
			divide(x0, y0, w, wy - y0);
			divide(x0, wy, w, h - (wy - y0));
		} else {
			// Build "vertical wall" at random x from [x0 + 1, x0 + w - 1], keep random door
			int wx = min(x0 + 1 + rnd.nextInt(w - 1), grid.numCols() - 1);
			int door = rnd.nextInt(h);
			range(0, h).filter(y -> y != door).map(y -> y0 + y).forEach(y -> {
				grid.edge(grid.cell(wx - 1, y), grid.cell(wx, y)).ifPresent(grid::removeEdge);
			});
			divide(x0, y0, wx - x0, h);
			divide(wx, y0, w - (wx - x0), h);
		}
	}
}