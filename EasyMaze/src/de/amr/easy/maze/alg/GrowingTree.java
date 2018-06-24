package de.amr.easy.maze.alg;

import static de.amr.easy.graph.api.traversal.TraversalState.COMPLETED;
import static de.amr.easy.graph.api.traversal.TraversalState.UNVISITED;
import static de.amr.easy.util.StreamUtils.permute;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import de.amr.easy.maze.alg.core.ObservableMazeGenerator;
import de.amr.easy.maze.alg.core.OrthogonalGrid;

/**
 * The "Growing-Tree" algorithm.
 * 
 * @author Armin Reichert
 * 
 * @see <a href=
 *      "http://weblog.jamisbuck.org/2011/1/27/maze-generation-growing-tree-algorithm.html">Maze
 *      Generation: Growing Tree algorithm</a>
 */
public class GrowingTree extends ObservableMazeGenerator {

	public GrowingTree(int numCols, int numRows) {
		super(numCols, numRows, false, UNVISITED);
	}

	private final Random rnd = new Random();

	@Override
	public OrthogonalGrid createMaze(int x, int y) {
		List<Integer> cells = new ArrayList<>();
		cells.add(maze.cell(x, y));
		do {
			int index = rnd.nextBoolean() ? cells.size() - 1 : rnd.nextInt(cells.size());
			int cell = cells.remove(index);
			permute(maze.neighbors(cell)).filter(maze::isUnvisited).forEach(neighbor -> {
				maze.addEdge(cell, neighbor);
				maze.set(cell, COMPLETED);
				maze.set(neighbor, COMPLETED);
				cells.add(neighbor);
			});
		} while (!cells.isEmpty());
		return maze;
	}
}