package de.amr.easy.maze.algorithms;

import static de.amr.easy.graph.api.TraversalState.COMPLETED;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

import de.amr.easy.graph.api.TraversalState;
import de.amr.easy.graph.impl.DefaultEdge;
import de.amr.easy.grid.api.ObservableDataGrid2D;
import de.amr.easy.maze.datastructures.Partition;
import de.amr.easy.maze.datastructures.Partition.EquivClass;

/**
 * Maze generator derived from Kruskal's minimum spanning tree algorithm.
 * 
 * <p>
 * Instead of always selecting the edge with minimal weight, the maze algorithm shuffles the edge
 * set of a full grid and takes the edges from this list.
 * 
 * @author Armin Reichert
 */
public class KruskalMST implements Consumer<Integer> {

	private final ObservableDataGrid2D<Integer, DefaultEdge<Integer>, TraversalState> grid;
	// Note: The partition has not to be initialized because the find-operation
	// creates new equivalence-classes on demand.
	private final Partition<Integer> forest = new Partition<>();

	public KruskalMST(ObservableDataGrid2D<Integer, DefaultEdge<Integer>, TraversalState> grid) {
		this.grid = grid;
	}

	@Override
	public void accept(Integer start) {
		fullGridEdgesInRandomOrder().forEach(edge -> {
			Integer either = edge.either(), other = edge.other(either);
			EquivClass eitherTree = forest.find(either), otherTree = forest.find(other);
			if (eitherTree != otherTree) {
				grid.set(either, COMPLETED);
				grid.set(other, COMPLETED);
				grid.addEdge(edge);
				forest.union(eitherTree, otherTree);
			}
		});
	}

	private Iterable<DefaultEdge<Integer>> fullGridEdgesInRandomOrder() {
		int fullGridEdgeCount = 2 * grid.numCols() * grid.numRows() - grid.numRows() - grid.numCols();
		List<DefaultEdge<Integer>> edges = new ArrayList<>(fullGridEdgeCount);
		grid.setEventsEnabled(false);
		grid.fillAllEdges();
		grid.edgeStream().forEach(edges::add);
		grid.removeEdges();
		grid.setEventsEnabled(true);
		Collections.shuffle(edges); // takes linear time
		return edges;
	}
}
