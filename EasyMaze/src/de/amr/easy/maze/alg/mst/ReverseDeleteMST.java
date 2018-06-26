package de.amr.easy.maze.alg.mst;

import static de.amr.easy.graph.api.traversal.TraversalState.COMPLETED;
import static de.amr.easy.util.StreamUtils.permute;

import de.amr.easy.graph.api.Edge;
import de.amr.easy.maze.alg.core.OrthogonalMazeGenerator;
import de.amr.easy.maze.alg.core.OrthogonalGrid;

/**
 * Maze generator derived from the Reverse-Delete-MST algorithm.
 * 
 * @author Armin Reichert
 *
 * @see <a href="https://en.wikipedia.org/wiki/Reverse-delete_algorithm">Wikipedia</a>
 */
public abstract class ReverseDeleteMST extends OrthogonalMazeGenerator {

	public ReverseDeleteMST(int numCols, int numRows) {
		super(numCols, numRows, true, COMPLETED);
	}

	@Override
	public OrthogonalGrid createMaze(int x, int y) {
		Iterable<Edge<Void>> edges = permute(maze.edges())::iterator;
		for (Edge<Void> edge : edges) {
			if (maze.numEdges() == maze.numVertices() - 1) {
				break;
			}
			maze.removeEdge(edge);
			if (!connected(edge.either(), edge.other())) {
				maze.addEdge(edge.either(), edge.other());
			}
		}
		return maze;
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