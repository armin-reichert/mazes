package de.amr.easy.maze.alg;

import static de.amr.easy.graph.api.traversal.TraversalState.COMPLETED;
import static de.amr.easy.graph.api.traversal.TraversalState.UNVISITED;
import static de.amr.easy.grid.impl.Top4.E;
import static de.amr.easy.grid.impl.Top4.S;

import java.util.OptionalInt;
import java.util.Random;
import java.util.stream.IntStream;

import de.amr.easy.maze.alg.core.OrthogonalMazeGenerator;
import de.amr.easy.maze.alg.core.OrthogonalGrid;

/**
 * Creates a random binary spanning tree.
 * 
 * @author Armin Reichert
 * 
 * @see <a href=
 *      "http://weblog.jamisbuck.org/2011/2/1/maze-generation-binary-tree-algorithm.html">Maze
 *      Generation: Binary Tree algorithm</a>
 */
public class BinaryTree extends OrthogonalMazeGenerator {

	protected final Random rnd = new Random();

	public BinaryTree(int numCols, int numRows) {
		super(numCols, numRows, false, UNVISITED);
	}

	@Override
	public OrthogonalGrid createMaze(int x, int y) {
		cells().forEach(v -> findRandomParent(v, S, E).ifPresent(parent -> {
			maze.addEdge(v, parent);
			maze.set(v, COMPLETED);
			maze.set(parent, COMPLETED);
		}));
		return maze;
	}

	protected IntStream cells() {
		return maze.vertices();
	}

	private OptionalInt findRandomParent(int cell, int dir1, int dir2) {
		boolean choice = rnd.nextBoolean();
		OptionalInt neighbor = maze.neighbor(cell, choice ? dir1 : dir2);
		return neighbor.isPresent() ? neighbor : maze.neighbor(cell, choice ? dir2 : dir1);
	}
}