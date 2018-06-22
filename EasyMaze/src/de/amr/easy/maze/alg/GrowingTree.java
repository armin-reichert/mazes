package de.amr.easy.maze.alg;

import static de.amr.easy.graph.api.traversal.TraversalState.COMPLETED;
import static de.amr.easy.util.StreamUtils.permute;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import de.amr.easy.maze.alg.core.MazeGenerator;
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
public class GrowingTree implements MazeGenerator {

	private final OrthogonalGrid grid;
	private final Random rnd = new Random();

	public GrowingTree(OrthogonalGrid grid) {
		this.grid = grid;
	}

	@Override
	public void run(int start) {
		List<Integer> cells = new ArrayList<>();
		cells.add(start);
		do {
			int index = rnd.nextBoolean() ? cells.size() - 1 : rnd.nextInt(cells.size());
			int cell = cells.remove(index);
			permute(grid.neighbors(cell)).filter(grid::isUnvisited).forEach(neighbor -> {
				grid.addEdge(cell, neighbor);
				grid.set(cell, COMPLETED);
				grid.set(neighbor, COMPLETED);
				cells.add(neighbor);
			});
		} while (!cells.isEmpty());
	}
}