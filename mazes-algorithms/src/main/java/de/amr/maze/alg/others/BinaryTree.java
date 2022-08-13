package de.amr.maze.alg.others;

import static de.amr.graph.core.api.TraversalState.COMPLETED;
import static de.amr.graph.grid.api.GridPosition.BOTTOM_LEFT;
import static de.amr.graph.grid.api.GridPosition.BOTTOM_RIGHT;
import static de.amr.graph.grid.api.GridPosition.CENTER;
import static de.amr.graph.grid.api.GridPosition.TOP_LEFT;
import static de.amr.graph.grid.api.GridPosition.TOP_RIGHT;
import static de.amr.graph.grid.impl.Grid4Topology.E;
import static de.amr.graph.grid.impl.Grid4Topology.N;
import static de.amr.graph.grid.impl.Grid4Topology.S;
import static de.amr.graph.grid.impl.Grid4Topology.W;

import java.util.Arrays;
import java.util.Map;
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
 * @see <a href= "http://weblog.jamisbuck.org/2011/2/1/maze-generation-binary-tree-algorithm.html">Maze Generation:
 *      Binary Tree algorithm</a>
 */
public class BinaryTree extends MazeGenerator {

	static record Branch(byte first, byte second) {
	}

	static final Map<GridPosition, Branch> BRANCH_BY_ROOT_POS = Map.of(//
			TOP_LEFT, new Branch(N, W), //
			TOP_RIGHT, new Branch(N, E), //
			CENTER, new Branch(N, W), //
			BOTTOM_LEFT, new Branch(S, W), //
			BOTTOM_RIGHT, new Branch(S, E));

	public BinaryTree(GridGraph2D<TraversalState, Integer> grid) {
		super(grid);
	}

	@Override
	public void createMaze(int x, int y) {
		var rootPosition = Arrays.stream(GridPosition.values()).filter(pos -> grid.cell(pos) == grid.cell(x, y)).findFirst()
				.orElse(TOP_LEFT);
		var branch = BRANCH_BY_ROOT_POS.get(rootPosition);
		cells().forEach(v -> findRandomParent(v, branch.first, branch.second).ifPresent(parent -> {
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
		var neighbor = grid.neighbor(cell, choice ? dir1 : dir2);
		if (neighbor.isPresent()) {
			return neighbor;
		}
		return grid.neighbor(cell, choice ? dir2 : dir1);
	}
}