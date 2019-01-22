package de.amr.easy.maze.alg.mst;

import static de.amr.easy.graph.grid.impl.OrthogonalGrid.fullGrid;
import static de.amr.easy.graph.pathfinder.api.TraversalState.COMPLETED;
import static de.amr.easy.util.StreamUtils.permute;

import de.amr.easy.graph.core.api.Edge;
import de.amr.easy.graph.grid.impl.OrthogonalGrid;
import de.amr.easy.maze.alg.core.MazeGenerator;

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