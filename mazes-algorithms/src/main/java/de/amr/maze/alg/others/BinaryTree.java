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
import java.util.EnumMap;
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

	static final EnumMap<GridPosition, byte[]> branchingByRootPosition = new EnumMap<>(GridPosition.class);

	static {
		branchingByRootPosition.put(TOP_LEFT, new byte[] { N, W });
		branchingByRootPosition.put(TOP_RIGHT, new byte[] { N, E });
		branchingByRootPosition.put(CENTER, new byte[] { N, W });
		branchingByRootPosition.put(BOTTOM_LEFT, new byte[] { S, W });
		branchingByRootPosition.put(BOTTOM_RIGHT, new byte[] { S, E });
	}

	public BinaryTree(GridGraph2D<TraversalState, Integer> grid) {
		super(grid);
	}

	@Override
	public void createMaze(int x, int y) {
		GridPosition rootPosition = Arrays.stream(GridPosition.values()).filter(pos -> grid.cell(pos) == grid.cell(x, y))
				.findFirst().orElse(TOP_LEFT);
		byte[] branching = branchingByRootPosition.get(rootPosition);
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
}