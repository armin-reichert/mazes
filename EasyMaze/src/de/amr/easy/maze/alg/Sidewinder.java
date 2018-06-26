package de.amr.easy.maze.alg;

import static de.amr.easy.graph.api.traversal.TraversalState.COMPLETED;
import static de.amr.easy.graph.api.traversal.TraversalState.UNVISITED;
import static java.util.stream.IntStream.range;

import java.util.Random;

import de.amr.easy.maze.alg.core.OrthogonalMazeGenerator;
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
public class Sidewinder extends OrthogonalMazeGenerator {

	private final Random rnd = new Random();
	private int current;

	public Sidewinder(int numCols, int numRows) {
		super(numCols, numRows, false, UNVISITED);
	}

	@Override
	public OrthogonalGrid createMaze(int x, int y) {
		range(0, maze.numRows()).forEach(row -> {
			current = 0;
			range(0, maze.numCols()).forEach(col -> {
				if (row > 0 && (col == maze.numCols() - 1 || rnd.nextBoolean())) {
					int passageCol = current + rnd.nextInt(col - current + 1);
					int north = maze.cell(passageCol, row - 1), south = maze.cell(passageCol, row);
					maze.addEdge(north, south);
					maze.set(north, COMPLETED);
					maze.set(south, COMPLETED);
					current = col + 1;
				} else if (col + 1 < maze.numCols()) {
					int west = maze.cell(col, row), east = maze.cell(col + 1, row);
					maze.addEdge(west, east);
					maze.set(west, COMPLETED);
					maze.set(east, COMPLETED);
				}
			});
		});
		return maze;
	}
}