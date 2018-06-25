package de.amr.easy.maze.alg;

import static de.amr.easy.graph.api.traversal.TraversalState.COMPLETED;
import static java.lang.Math.min;
import static java.util.stream.IntStream.range;

import java.util.Random;

import de.amr.easy.maze.alg.core.ObservableMazeGenerator;
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
public class RecursiveDivision extends ObservableMazeGenerator {

	private final Random rnd = new Random();

	public RecursiveDivision(int numCols, int numRows) {
		super(numCols, numRows, true, COMPLETED);
	}

	@Override
	public OrthogonalGrid createMaze(int x, int y) {
		divide(0, 0, maze.numCols(), maze.numRows());
		return maze;
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
			int wy = min(y0 + 1 + rnd.nextInt(h - 1), maze.numRows() - 1);
			int door = rnd.nextInt(w);
			range(0, w).filter(x -> x != door).map(x -> x0 + x).forEach(x -> {
				maze.edge(maze.cell(x, wy - 1), maze.cell(x, wy)).ifPresent(maze::removeEdge);
			});
			divide(x0, y0, w, wy - y0);
			divide(x0, wy, w, h - (wy - y0));
		} else {
			// Build "vertical wall" at random x from [x0 + 1, x0 + w - 1], keep random door
			int wx = min(x0 + 1 + rnd.nextInt(w - 1), maze.numCols() - 1);
			int door = rnd.nextInt(h);
			range(0, h).filter(y -> y != door).map(y -> y0 + y).forEach(y -> {
				maze.edge(maze.cell(wx - 1, y), maze.cell(wx, y)).ifPresent(maze::removeEdge);
			});
			divide(x0, y0, wx - x0, h);
			divide(wx, y0, w - (wx - x0), h);
		}
	}
}