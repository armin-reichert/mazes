package de.amr.easy.maze.alg.mst;

import static de.amr.easy.graph.api.TraversalState.COMPLETED;
import static de.amr.easy.util.StreamUtils.permute;

import de.amr.easy.graph.alg.traversal.BreadthFirstTraversal;
import de.amr.easy.graph.api.TraversalState;
import de.amr.easy.graph.api.WeightedEdge;
import de.amr.easy.grid.api.Grid2D;
import de.amr.easy.maze.alg.MazeAlgorithm;

/**
 * A (naive?) implementation of the Reverse-Delete-MST algorithm.
 * 
 * @author Armin Reichert
 *
 * @see <a href="https://en.wikipedia.org/wiki/Reverse-delete_algorithm">Wikipedia</a>
 */
public class ReverseDeleteMST extends MazeAlgorithm {

	public ReverseDeleteMST(Grid2D<TraversalState, Integer> grid) {
		super(grid);
	}

	@Override
	public void run(int start) {
		grid.setDefaultContent(COMPLETED);
		grid.fill();
		Iterable<WeightedEdge<Integer>> edges = permute(grid.edgeStream())::iterator;
		for (WeightedEdge<Integer> edge : edges) {
			if (grid.edgeCount() == grid.vertexCount() - 1) {
				break;
			}
			int u = edge.either(), v = edge.other(u);
			grid.removeEdge(edge);
			if (disconnected(u, v)) {
				grid.addEdge(u, v);
			}
		}
	}

	private boolean disconnected(int u, int v) {
		BreadthFirstTraversal bfs = new BreadthFirstTraversal(grid, u);
		bfs.setStopAt(v);
		bfs.traverseGraph();
		return bfs.getDistance(v) == -1;
	}
}