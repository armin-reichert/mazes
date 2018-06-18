package de.amr.easy.maze.alg.mst;

import static de.amr.easy.graph.api.traversal.TraversalState.COMPLETED;
import static de.amr.easy.util.StreamUtils.permute;

import de.amr.easy.graph.api.SimpleEdge;
import de.amr.easy.graph.api.traversal.TraversalState;
import de.amr.easy.grid.api.GridGraph2D;
import de.amr.easy.maze.alg.core.MazeAlgorithm;

/**
 * Maze generator derived from the Reverse-Delete-MST algorithm.
 * 
 * @author Armin Reichert
 *
 * @see <a href="https://en.wikipedia.org/wiki/Reverse-delete_algorithm">Wikipedia</a>
 */
public abstract class ReverseDeleteMST extends MazeAlgorithm {

	public ReverseDeleteMST(GridGraph2D<TraversalState, SimpleEdge> grid) {
		super(grid);
	}

	@Override
	public void run(int start) {
		grid.setDefaultVertex(COMPLETED);
		grid.fill();
		Iterable<SimpleEdge> edges = permute(grid.edges())::iterator;
		for (SimpleEdge edge : edges) {
			if (grid.numEdges() == grid.numVertices() - 1) {
				break;
			}
			int u = edge.either(), v = edge.other();
			grid.removeEdge(edge);
			if (!connected(u, v)) {
				grid.addEdge(u, v);
			}
		}
	}

	/**
	 * 
	 * @param u
	 *          a cell
	 * @param v
	 *          a cell
	 * @return {@code true} if given cells are connected by some path
	 */
	protected abstract boolean connected(int u, int v);
}