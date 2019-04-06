package de.amr.maze.alg;

import static de.amr.graph.core.api.TraversalState.COMPLETED;
import static de.amr.graph.core.api.TraversalState.UNVISITED;
import static de.amr.graph.grid.impl.Top4.E;
import static de.amr.graph.grid.impl.Top4.S;

import java.util.OptionalInt;
import java.util.Random;
import java.util.stream.IntStream;

import de.amr.graph.core.api.TraversalState;
import de.amr.graph.grid.api.GridGraph2D;
import de.amr.maze.alg.core.MazeGenerator;
import de.amr.maze.alg.core.MazeGridFactory;

/**
 * Creates a random binary spanning tree.
 * 
 * @author Armin Reichert
 * 
 * @see <a href=
 *      "http://weblog.jamisbuck.org/2011/2/1/maze-generation-binary-tree-algorithm.html">Maze
 *      Generation: Binary Tree algorithm</a>
 */
public class BinaryTree implements MazeGenerator {

	protected GridGraph2D<TraversalState, Integer> grid;
	protected Random rnd = new Random();

	public BinaryTree(MazeGridFactory factory, int numCols, int numRows) {
		grid = factory.emptyGrid(numCols, numRows, UNVISITED);
	}

	@Override
	public GridGraph2D<TraversalState, Integer> getGrid() {
		return grid;
	}

	@Override
	public GridGraph2D<TraversalState, Integer> createMaze(int x, int y) {
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