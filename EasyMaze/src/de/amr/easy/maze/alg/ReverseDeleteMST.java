package de.amr.easy.maze.alg;

import static de.amr.easy.graph.api.TraversalState.COMPLETED;
import static java.util.stream.Collectors.toCollection;

import java.util.ArrayList;
import java.util.List;

import de.amr.easy.graph.alg.traversal.BreadthFirstTraversal;
import de.amr.easy.graph.api.TraversalState;
import de.amr.easy.graph.api.WeightedEdge;
import de.amr.easy.grid.api.Dir4;
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

	public ReverseDeleteMST(Grid2D<Dir4,TraversalState, Integer> grid) {
		super(grid);
		grid.makeFullGrid();
		grid.setDefaultContent(COMPLETED);
	}

	@Override
	public void accept(Integer cell) {
		/*@formatter:off*/
		List<WeightedEdge<Integer,Integer>> sortedEdgeList = grid.edgeStream()
				.map(this::setRandomEdgeWeight)
				.sorted()
				.collect(toCollection(ArrayList::new));
		/*@formatter:on*/
		while (!sortedEdgeList.isEmpty()) {
			WeightedEdge<Integer, Integer> maxWeightEdge = sortedEdgeList.remove(sortedEdgeList.size() - 1);
			Integer either = maxWeightEdge.either(), other = maxWeightEdge.other(either);
			grid.removeEdge(maxWeightEdge);
			if (!connected(either, other)) {
				grid.addEdge(either, other);
			}
		}
		// System.out.println("Reverse-Delete MST:");
		// System.out.println("#vertices: " + grid.vertexCount());
		// System.out.println("#edges: " + grid.edgeCount());
		// System.out.println(bfsCount + " BFS executions took " + bfsTotalTime + " seconds");
	}

	// TODO more efficient connectivity test or data-structure
	private boolean connected(Integer either, Integer other) {
		BreadthFirstTraversal<Integer, WeightedEdge<Integer, Integer>> bfs = new BreadthFirstTraversal<>(grid, either);
		bfs.setStopAt(other);
		StopWatch watch = new StopWatch();
		watch.runAndMeasure(bfs);
		++bfsCount;
		bfsTotalTime += watch.getSeconds();
		// System.out.println("BFS #" + bfsCount + " took " + watch.getDuration() + " seconds");
		return bfs.getDistance(other) != -1;
	}
}