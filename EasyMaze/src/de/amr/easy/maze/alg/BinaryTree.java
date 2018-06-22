package de.amr.easy.maze.alg;

import static de.amr.easy.graph.api.traversal.TraversalState.COMPLETED;
import static de.amr.easy.grid.impl.Top4.E;
import static de.amr.easy.grid.impl.Top4.S;

import java.util.OptionalInt;
import java.util.stream.IntStream;

import de.amr.easy.graph.api.traversal.TraversalState;
import de.amr.easy.grid.api.GridGraph2D;
import de.amr.easy.maze.alg.core.MazeAlgorithm;

/**
 * Creates a random binary spanning tree.
 * 
 * @author Armin Reichert
 * 
 * @see <a href=
 *      "http://weblog.jamisbuck.org/2011/2/1/maze-generation-binary-tree-algorithm.html">Maze
 *      Generation: Binary Tree algorithm</a>
 */
public class BinaryTree extends MazeAlgorithm<Void> {

	public BinaryTree(GridGraph2D<TraversalState, Void> grid) {
		super(grid);
	}

	@Override
	public void run(int start) {
		cells().forEach(v -> findRandomParent(v, S, E).ifPresent(parent -> {
			grid.addEdge(v, parent);
			grid.set(v, COMPLETED);
			grid.set(parent, COMPLETED);
		}));
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