package de.amr.easy.maze.algorithms;

import static de.amr.easy.graph.api.TraversalState.COMPLETED;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import de.amr.easy.graph.api.SimpleEdge;
import de.amr.easy.graph.api.TraversalState;
import de.amr.easy.grid.api.ObservableDataGrid2D;
import de.amr.easy.maze.datastructures.Partition;
import de.amr.easy.maze.datastructures.Partition.EquivClass;

/**
 * Maze generator derived from Kruskal's minimum spanning tree algorithm.
 * 
 * @author Armin Reichert
 */
public class KruskalMST extends MazeAlgorithm {

	private final Partition<Integer> forest = new Partition<>();

	public KruskalMST(ObservableDataGrid2D<TraversalState> grid) {
		super(grid);
	}

	@Override
	public void accept(Integer start) {
		fullGridEdgesInRandomOrder().forEach(edge -> {
			Integer either = edge.either(), other = edge.other(either);
			EquivClass eitherTree = forest.find(either), otherTree = forest.find(other);
			if (eitherTree != otherTree) {
				grid.set(either, COMPLETED);
				grid.set(other, COMPLETED);
				grid.addEdge(either, other);
				forest.union(eitherTree, otherTree);
			}
		});
	}

	private Stream<SimpleEdge<Integer>> fullGridEdgesInRandomOrder() {
		grid.setEventsEnabled(false);
		grid.makeFullGrid();
		List<SimpleEdge<Integer>> edges = grid.edgeStream().collect(Collectors.toList());
		Collections.shuffle(edges); // takes linear time
		grid.removeEdges();
		grid.setEventsEnabled(true);
		return edges.stream();
	}
}