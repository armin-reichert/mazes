package de.amr.maze.alg.mst;

import static de.amr.datastruct.StreamUtils.permute;
import static de.amr.graph.core.api.TraversalState.COMPLETED;
import static de.amr.maze.alg.core.OrthogonalGrid.fullGrid;

import de.amr.graph.core.api.Edge;
import de.amr.maze.alg.core.MazeGenerator;
import de.amr.maze.alg.core.OrthogonalGrid;

/**
 * Maze generator derived from the Reverse-Delete-MST algorithm.
 * 
 * @author Armin Reichert
 *
 * @see <a href="https://en.wikipedia.org/wiki/Reverse-delete_algorithm">Wikipedia</a>
 */
public abstract class ReverseDeleteMST implements MazeGenerator<OrthogonalGrid> {

	protected OrthogonalGrid grid;

	public ReverseDeleteMST(int numCols, int numRows) {
		grid = fullGrid(numCols, numRows, COMPLETED);
	}

	@Override
	public OrthogonalGrid getGrid() {
		return grid;
	}

	@Override
	public OrthogonalGrid createMaze(int x, int y) {
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