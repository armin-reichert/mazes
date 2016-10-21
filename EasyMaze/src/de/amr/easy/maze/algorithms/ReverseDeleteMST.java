package de.amr.easy.maze.algorithms;

import static de.amr.easy.graph.api.TraversalState.COMPLETED;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import de.amr.easy.graph.api.TraversalState;
import de.amr.easy.graph.api.WeightedEdge;
import de.amr.easy.graph.traversal.BreadthFirstTraversal;
import de.amr.easy.grid.api.Grid2D;
import de.amr.easy.grid.impl.ObservableGrid;

/**
 * 
 * @author Armin Reichert
 *
 * @see <a href="https://en.wikipedia.org/wiki/Reverse-delete_algorithm">Wikipedia</a>
 */
public class ReverseDeleteMST extends MazeAlgorithm {

	public ReverseDeleteMST(Grid2D<TraversalState> grid) {
		super(grid);
	}

	@Override
	public void accept(Integer cell) {
		List<WeightedEdge<Integer>> edges = fullGridEdges();
		while (grid.edgeCount() > grid.vertexCount() - 1) {
			WeightedEdge<Integer> edge = edges.remove(0);
			System.out.println(edge);
			Integer either = edge.either(), other = edge.other(either);
			grid.removeEdge(edge);
			if (!connected(either, other)) {
				grid.addEdge(either, other);
			}
		}
	}

	private boolean connected(Integer either, Integer other) {
		BreadthFirstTraversal<Integer, WeightedEdge<Integer>> bfs = new BreadthFirstTraversal<>(
				(ObservableGrid<TraversalState>) grid, either);
		bfs.run();
		return bfs.getDistance(other) != -1;
	}

	private List<WeightedEdge<Integer>> fullGridEdges() {
		grid.makeFullGrid();
		grid.setDefault(COMPLETED);
		List<WeightedEdge<Integer>> edges = grid.edgeStream().collect(Collectors.toList());
		edges.stream().forEach(edge -> edge.setWeight(rnd.nextDouble()));
		Collections.sort(edges, (e1, e2) -> e2.compareTo(e1)); // descending
		return edges;
	}
}