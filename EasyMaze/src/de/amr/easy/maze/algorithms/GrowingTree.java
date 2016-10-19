package de.amr.easy.maze.algorithms;

import static de.amr.easy.graph.api.TraversalState.COMPLETED;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import de.amr.easy.graph.api.TraversalState;
import de.amr.easy.grid.api.DataGrid2D;

/**
 * The "Growing-Tree" algorithm.
 * 
 * @author Armin Reichert
 * 
 * @see <a href=
 *      "http://weblog.jamisbuck.org/2011/1/27/maze-generation-growing-tree-algorithm.html">Buckblog:
 *      Maze Generation: Growing Tree algorithm</a>
 *
 */
public class GrowingTree extends MazeAlgorithm {

	protected final List<Integer> cells = new ArrayList<>();

	public GrowingTree(DataGrid2D<TraversalState> grid) {
		super(grid);
	}

	@Override
	public void accept(Integer start) {
		cells.add(start);
		while (!cells.isEmpty()) {
			Integer cell = selectCell();
			Optional<Integer> neighbor = grid.neighborsPermuted(cell).filter(this::isCellUnvisited).findAny();
			if (neighbor.isPresent()) {
				grid.addEdge(cell, neighbor.get());
				grid.set(cell, COMPLETED);
				grid.set(neighbor.get(), COMPLETED);
				cells.add(neighbor.get());
			} else {
				cells.remove(cell);
			}
		}
	}

	/**
	 * Subclasses might provide another selection strategy.
	 */
	protected Integer selectCell() {
		return rnd.nextBoolean() ? cells.get(rnd.nextInt(cells.size())) : cells.get(cells.size() - 1);
	}
}