package de.amr.easy.maze.alg;

import static de.amr.datastruct.StreamUtils.permute;
import static de.amr.graph.grid.impl.OrthogonalGrid.emptyGrid;
import static de.amr.graph.pathfinder.api.TraversalState.COMPLETED;
import static de.amr.graph.pathfinder.api.TraversalState.UNVISITED;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import de.amr.easy.maze.alg.core.MazeGenerator;
import de.amr.graph.grid.impl.OrthogonalGrid;

/**
 * The "Growing-Tree" algorithm.
 * 
 * @author Armin Reichert
 * 
 * @see <a href=
 *      "http://weblog.jamisbuck.org/2011/1/27/maze-generation-growing-tree-algorithm.html">Maze
 *      Generation: Growing Tree algorithm</a>
 */
public class GrowingTree implements MazeGenerator<OrthogonalGrid> {

	private OrthogonalGrid grid;
	private Random rnd = new Random();

	public GrowingTree(int numCols, int numRows) {
		grid = emptyGrid(numCols, numRows, UNVISITED);
	}

	@Override
	public OrthogonalGrid getGrid() {
		return grid;
	}

	@Override
	public OrthogonalGrid createMaze(int x, int y) {
		List<Integer> cells = new ArrayList<>();
		cells.add(grid.cell(x, y));
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
		return grid;
	}
}