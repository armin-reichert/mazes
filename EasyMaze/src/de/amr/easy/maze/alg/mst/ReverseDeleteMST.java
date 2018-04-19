package de.amr.easy.maze.alg.mst;

import static de.amr.easy.graph.api.TraversalState.COMPLETED;
import static java.util.stream.Collectors.toCollection;

import java.util.ArrayList;
import java.util.List;

import de.amr.easy.graph.alg.traversal.BreadthFirstTraversal;
import de.amr.easy.graph.api.TraversalState;
import de.amr.easy.graph.api.WeightedEdge;
import de.amr.easy.grid.api.Grid2D;
import de.amr.easy.maze.alg.MazeAlgorithm;

/**
 * A naive implementation of the Reverse-Delete-algorithm.
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
	public void run(int cell) {
		grid.setDefaultContent(COMPLETED);
		grid.fill();
		/*@formatter:off*/
		List<WeightedEdge<Integer>> sortedEdges = grid.edgeStream()
				.map(this::setRandomEdgeWeight)
				.sorted()
				.collect(toCollection(ArrayList::new));
		/*@formatter:on*/
		while (grid.edgeCount() < grid.vertexCount() - 1 || !sortedEdges.isEmpty()) {
			WeightedEdge<Integer> edge = sortedEdges.remove(sortedEdges.size() - 1);
			int u = edge.either(), v = edge.other(u);
			grid.removeEdge(edge);
			if (!connected(u, v)) {
				grid.addEdge(u, v);
			}
		}
	}

	// TODO more efficient connectivity test or data-structure
	private boolean connected(int either, int other) {
		BreadthFirstTraversal<WeightedEdge<Integer>> bfs = new BreadthFirstTraversal<>(grid, either);
		bfs.setStopAt(other);
		bfs.run();
		return bfs.getDistance(other) != -1;
	}
}