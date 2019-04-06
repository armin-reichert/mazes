package de.amr.maze.alg.mst;

import static de.amr.datastruct.StreamUtils.permute;

import de.amr.graph.core.api.Edge;
import de.amr.graph.core.api.TraversalState;
import de.amr.graph.grid.api.GridGraph2D;
import de.amr.maze.alg.core.MazeGridFactory;
import de.amr.maze.alg.core.MazeGenerator;

/**
 * Maze generator derived from the Reverse-Delete-MST algorithm.
 * 
 * @author Armin Reichert
 *
 * @see <a href="https://en.wikipedia.org/wiki/Reverse-delete_algorithm">Wikipedia</a>
 */
public abstract class ReverseDeleteMST implements MazeGenerator {

	protected GridGraph2D<TraversalState, Integer> grid;

	public ReverseDeleteMST(MazeGridFactory factory, int numCols, int numRows) {
		grid = factory.fullGrid(numCols, numRows, TraversalState.COMPLETED);
	}

	@Override
	public GridGraph2D<TraversalState, Integer> getGrid() {
		return grid;
	}

	@Override
	public GridGraph2D<TraversalState, Integer> createMaze(int x, int y) {
		Iterable<Edge> edges = permute(grid.edges())::iterator;
		for (Edge edge : edges) {
			if (grid.numEdges() == grid.numVertices() - 1) {
				break;
			}
			int u = edge.either(), v = edge.other();
			grid.removeEdge(u, v);
			if (!connected(u, v)) {
				grid.addEdge(u, v);
			}
		}
		return grid;
	}

	/**
	 * @param u
	 *            a cell
	 * @param v
	 *            a cell
	 * @return {@code true} if given cells are connected by some path
	 */
	protected abstract boolean connected(int u, int v);
}