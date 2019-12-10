package de.amr.maze.alg.others;

import static de.amr.graph.core.api.TraversalState.COMPLETED;
import static de.amr.graph.grid.impl.Grid4Topology.E;
import static de.amr.graph.grid.impl.Grid4Topology.N;
import static de.amr.graph.grid.impl.Grid4Topology.S;
import static de.amr.graph.grid.impl.Grid4Topology.W;

import java.util.Optional;
import java.util.stream.IntStream;

import de.amr.graph.core.api.TraversalState;
import de.amr.graph.grid.api.GridGraph2D;
import de.amr.graph.grid.api.GridPosition;
import de.amr.maze.alg.core.MazeGenerator;

/**
 * Creates a random binary spanning tree.
 * 
 * @author Armin Reichert
 * 
 * @see <a href=
 *      "http://weblog.jamisbuck.org/2011/2/1/maze-generation-binary-tree-algorithm.html">Maze
 *      Generation: Binary Tree algorithm</a>
 */
public class BinaryTree extends MazeGenerator {

	public GridPosition rootPosition = GridPosition.TOP_LEFT;

	public BinaryTree(GridGraph2D<TraversalState, Integer> grid) {
		super(grid);
	}

	@Override
	public void createMaze(int x, int y) {
		byte[] branching = getBranching();
		cells().forEach(v -> findRandomParent(v, branching[0], branching[1]).ifPresent(parent -> {
			grid.addEdge(v, parent);
			grid.set(v, COMPLETED);
			grid.set(parent, COMPLETED);
		}));
	}

	protected IntStream cells() {
		return grid.vertices();
	}

	private Optional<Integer> findRandomParent(int cell, byte dir1, byte dir2) {
		boolean choice = rnd.nextBoolean();
		Optional<Integer> neighbor = grid.neighbor(cell, choice ? dir1 : dir2);
		return neighbor.isPresent() ? neighbor : grid.neighbor(cell, choice ? dir2 : dir1);
	}

	private byte[] getBranching() {
		switch (rootPosition) {
		case BOTTOM_LEFT:
			return new byte[] { S, W };
		case BOTTOM_RIGHT:
			return new byte[] { S, E };
		case TOP_LEFT:
			return new byte[] { N, W };
		case TOP_RIGHT:
			return new byte[] { N, E };
		default:
			return new byte[] { N, W };
		}
	}
}