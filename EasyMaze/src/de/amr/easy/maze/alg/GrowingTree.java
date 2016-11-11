package de.amr.easy.maze.alg;

import static de.amr.easy.graph.api.TraversalState.COMPLETED;

import java.util.ArrayList;
import java.util.List;

import de.amr.easy.graph.api.TraversalState;
import de.amr.easy.grid.api.Grid2D;

/**
 * The "Growing-Tree" algorithm.
 * 
 * @author Armin Reichert
 * 
 * @see <a href=
 *      "http://weblog.jamisbuck.org/2011/1/27/maze-generation-growing-tree-algorithm.html">Maze
 *      Generation: Growing Tree algorithm</a>
 */
public class GrowingTree extends MazeAlgorithm {

	private final List<Integer> cells = new ArrayList<>();

	public GrowingTree(Grid2D<TraversalState, Integer> grid) {
		super(grid);
	}

	@Override
	public void accept(Integer start) {
		cells.add(start);
		do {
			Integer cell = selectCell();
			grid.neighborsPermuted(cell).filter(this::isCellUnvisited).forEach(neighbor -> {
				grid.addEdge(cell, neighbor);
				grid.set(cell, COMPLETED);
				grid.set(neighbor, COMPLETED);
				cells.add(neighbor);
			});
			cells.remove(cell);
		} while (!cells.isEmpty());
	}

	/**
	 * Subclasses might provide another selection strategy.
	 */
	protected Integer selectCell() {
		return rnd.nextBoolean() ? cells.get(cells.size() - 1) : cells.get(rnd.nextInt(cells.size()));
	}
}