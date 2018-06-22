package de.amr.easy.maze.alg;

import static de.amr.easy.util.StreamUtils.permute;

import java.util.ArrayList;
import java.util.List;

import de.amr.easy.graph.api.traversal.TraversalState;
import de.amr.easy.grid.api.GridGraph2D;
import de.amr.easy.maze.alg.core.MazeAlgorithm;

/**
 * The "Growing-Tree" algorithm.
 * 
 * @author Armin Reichert
 * 
 * @see <a href=
 *      "http://weblog.jamisbuck.org/2011/1/27/maze-generation-growing-tree-algorithm.html">Maze
 *      Generation: Growing Tree algorithm</a>
 */
public class GrowingTree extends MazeAlgorithm<Void> {

	public GrowingTree(GridGraph2D<TraversalState, Void> grid) {
		super(grid);
	}

	@Override
	public void run(int start) {
		List<Integer> cells = new ArrayList<>();
		cells.add(start);
		do {
			int index = rnd.nextBoolean() ? cells.size() - 1 : rnd.nextInt(cells.size());
			int cell = cells.remove(index);
			permute(grid.neighbors(cell)).filter(isCellUnvisited).forEach(neighbor -> {
				addTreeEdge(cell, neighbor);
				cells.add(neighbor);
			});
		} while (!cells.isEmpty());
	}
}