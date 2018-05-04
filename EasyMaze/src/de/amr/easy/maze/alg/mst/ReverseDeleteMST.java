package de.amr.easy.maze.alg.mst;

import static de.amr.easy.graph.api.TraversalState.COMPLETED;
import static java.util.stream.Collectors.toCollection;

import java.util.Collections;
import java.util.LinkedList;

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
		LinkedList<WeightedEdge<Integer>> edgeList = grid.edgeStream().collect(toCollection(LinkedList::new));
		edgeList.forEach(edge -> edge.setWeight(rnd.nextInt()));
		Collections.sort(edgeList);
		while (grid.edgeCount() > grid.vertexCount() - 1) {
			WeightedEdge<Integer> maxEdge = edgeList.pollLast();
			grid.removeEdge(maxEdge);
			int u = maxEdge.either(), v = maxEdge.other(u);
			if (disconnected(u, v)) {
				grid.addEdge(u, v);
			}
		}
	}

	private boolean disconnected(int u, int v) {
		BreadthFirstTraversal<?> bfs = new BreadthFirstTraversal<>(grid, u);
		bfs.setStopAt(v);
		bfs.run();
		return bfs.getDistance(v) == -1;
	}
}