package de.amr.easy.maze.alg;

import static de.amr.easy.graph.grid.impl.OrthogonalGrid.emptyGrid;
import static de.amr.easy.graph.grid.impl.Top4.E;
import static de.amr.easy.graph.grid.impl.Top4.S;
import static de.amr.easy.graph.pathfinder.api.TraversalState.COMPLETED;
import static de.amr.easy.graph.pathfinder.api.TraversalState.UNVISITED;

import java.util.OptionalInt;
import java.util.Random;
import java.util.stream.IntStream;

import de.amr.easy.graph.grid.impl.OrthogonalGrid;
import de.amr.easy.maze.alg.core.MazeGenerator;

/**
 * Creates a random binary spanning tree.
 * 
 * @author Armin Reichert
 * 
 * @see <a href=
 *      "http://weblog.jamisbuck.org/2011/2/1/maze-generation-binary-tree-algorithm.html">Maze
 *      Generation: Binary Tree algorithm</a>
 */
public class BinaryTree implements MazeGenerator<OrthogonalGrid> {

	protected OrthogonalGrid grid;
	protected Random rnd = new Random();

	public BinaryTree(int numCols, int numRows) {
		grid = emptyGrid(numCols, numRows, UNVISITED);
	}

	@Override
	public OrthogonalGrid getGrid() {
		return grid;
	}

	@Override
	public OrthogonalGrid createMaze(int x, int y) {
		cells().forEach(v -> findRandomParent(v, S, E).ifPresent(parent -> {
			grid.addEdge(v, parent);
			grid.set(v, COMPLETED);
			grid.set(parent, COMPLETED);
		}));
		return grid;
	}

	protected IntStream cells() {
		return grid.vertices();
	}

	private OptionalInt findRandomParent(int cell, int dir1, int dir2) {
		boolean choice = rnd.nextBoolean();
		OptionalInt neighbor = grid.neighbor(cell, choice ? dir1 : dir2);
		return neighbor.isPresent() ? neighbor : grid.neighbor(cell, choice ? dir2 : dir1);
	}
}