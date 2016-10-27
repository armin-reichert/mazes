package de.amr.easy.maze.alg;

import static de.amr.easy.graph.api.TraversalState.COMPLETED;
import static java.util.stream.Collectors.toCollection;

import java.util.ArrayList;
import java.util.List;

import de.amr.easy.graph.alg.traversal.BreadthFirstTraversal;
import de.amr.easy.graph.api.TraversalState;
import de.amr.easy.graph.api.WeightedEdge;
import de.amr.easy.grid.api.Grid2D;
import de.amr.easy.util.StopWatch;

/**
 * A naive implementation of the Reverse-Delete-algorithm.
 * 
 * @author Armin Reichert
 *
 * @see <a href="https://en.wikipedia.org/wiki/Reverse-delete_algorithm">Wikipedia</a>
 */
public class ReverseDeleteMST extends MazeAlgorithm {

	private int bfsCount;
	private float bfsTotalTime;

	public ReverseDeleteMST(Grid2D<TraversalState> grid) {
		super(grid);
	}

	@Override
	public void accept(Integer cell) {
		grid.makeFullGrid();
		grid.setDefault(COMPLETED);
		List<WeightedEdge<Integer>> edges = fullGridEdgesSortedDescending();
		while (grid.edgeCount() > grid.vertexCount() - 1) {
			WeightedEdge<Integer> maxEdge = edges.remove(0);
			Integer either = maxEdge.either(), other = maxEdge.other(either);
			grid.removeEdge(maxEdge);
			if (!connected(either, other)) {
				grid.addEdge(either, other);
			}
		}
		// System.out.println("Reverse-Delete MST:");
		// System.out.println("#vertices: " + grid.vertexCount());
		// System.out.println("#edges: " + grid.edgeCount());
		// System.out.println(bfsCount + " BFS executions took " + bfsTotalTime + " seconds");
	}

	// TODO a more efficient connectivity test
	private boolean connected(Integer either, Integer other) {
		BreadthFirstTraversal<Integer, WeightedEdge<Integer>> bfs = new BreadthFirstTraversal<>(grid, either);
		bfs.setStopAt(other);
		StopWatch watch = new StopWatch();
		watch.runAndMeasure(bfs);
		++bfsCount;
		bfsTotalTime += watch.getSeconds();
		// System.out.println("BFS #" + bfsCount + " took " + watch.getDuration() + " seconds");
		return bfs.getDistance(other) != -1;
	}

	private List<WeightedEdge<Integer>> fullGridEdgesSortedDescending() {
		/*@formatter:off*/
		return grid.makeFullGrid().edgeStream()
			.map(edge -> {
				edge.setWeight(rnd.nextDouble());
				return edge;
			})
			.sorted((e1, e2) -> e2.compareTo(e1))
			.collect(toCollection(ArrayList::new));
		/*@formatter:on*/
	}
}