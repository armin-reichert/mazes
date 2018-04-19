package de.amr.easy.maze.alg;

import java.util.OptionalInt;
import java.util.stream.IntStream;

import de.amr.easy.graph.api.TraversalState;
import de.amr.easy.grid.api.Grid2D;
import de.amr.easy.grid.impl.Top4;

/**
 * Creates a random binary spanning tree.
 * 
 * @author Armin Reichert
 * 
 * @see <a href=
 *      "http://weblog.jamisbuck.org/2011/2/1/maze-generation-binary-tree-algorithm.html">Maze
 *      Generation: Binary Tree algorithm</a>
 */
public class BinaryTree extends MazeAlgorithm {

	public BinaryTree(Grid2D<TraversalState, Integer> grid) {
		super(grid);
	}

	@Override
	public void run(int start) {
		cellStream().forEach(cell -> {
			randomNeighbor(cell, Top4.S, Top4.E).ifPresent(neighbor -> {
				grid.addEdge(cell, neighbor);
				grid.set(cell, TraversalState.COMPLETED);
				grid.set(neighbor, TraversalState.COMPLETED);
			});
		});
	}

	/**
	 * 
	 * @param cell
	 *          a grid cell
	 * @param firstDir
	 *          first direction of tree
	 * @param secondDir
	 *          second direction of tree
	 * @return a random neighbor towards one of the given directions or nothing
	 */
	private OptionalInt randomNeighbor(int cell, int firstDir, int secondDir) {
		boolean choice = rnd.nextBoolean();
		OptionalInt neighbor = grid.neighbor(cell, choice ? firstDir : secondDir);
		return neighbor.isPresent() ? neighbor : grid.neighbor(cell, choice ? secondDir : firstDir);
	}

	/*
	 * @return stream of all grid cells in the order used for maze creation
	 */
	protected IntStream cellStream() {
		return grid.vertexStream();
	}
}
