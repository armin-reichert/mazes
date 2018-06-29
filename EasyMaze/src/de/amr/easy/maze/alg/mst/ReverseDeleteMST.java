package de.amr.easy.maze.alg.mst;

import static de.amr.easy.graph.api.traversal.TraversalState.COMPLETED;
import static de.amr.easy.util.StreamUtils.permute;

import de.amr.easy.graph.api.Edge;
import de.amr.easy.maze.alg.core.OrthogonalGrid;
import de.amr.easy.maze.alg.core.OrthogonalMazeGenerator;

/**
 * Maze generator derived from the Reverse-Delete-MST algorithm.
 * 
 * @author Armin Reichert
 *
 * @see <a href="https://en.wikipedia.org/wiki/Reverse-delete_algorithm">Wikipedia</a>
 */
public abstract class ReverseDeleteMST implements OrthogonalMazeGenerator {

	protected OrthogonalGrid grid;
	
	public ReverseDeleteMST(int numCols, int numRows) {
		grid = OrthogonalGrid.fullGrid(numCols, numRows, COMPLETED);
	}
	
	@Override
	public OrthogonalGrid getGrid() {
		return grid;
	}

	@Override
	public OrthogonalGrid createMaze(int x, int y) {
		Iterable<Edge<Void>> edges = permute(grid.edges())::iterator;
		for (Edge<Void> edge : edges) {
			if (grid.numEdges() == grid.numVertices() - 1) {
				break;
			}
			grid.removeEdge(edge);
			if (!connected(edge.either(), edge.other())) {
				grid.addEdge(edge.either(), edge.other());
			}
		}
		return grid;
	}

	/**
	 * @param u
	 *          a cell
	 * @param v
	 *          a cell
	 * @return {@code true} if given cells are connected by some path
	 */
	protected abstract boolean connected(int u, int v);
}