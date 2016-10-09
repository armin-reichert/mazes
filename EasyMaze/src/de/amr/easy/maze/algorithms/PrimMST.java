package de.amr.easy.maze.algorithms;

import static de.amr.easy.graph.api.TraversalState.COMPLETED;
import static de.amr.easy.graph.api.TraversalState.VISITED;

import java.util.PriorityQueue;
import java.util.Random;
import java.util.function.Consumer;

import de.amr.easy.graph.api.TraversalState;
import de.amr.easy.graph.impl.DefaultEdge;
import de.amr.easy.graph.impl.DefaultWeightedEdge;
import de.amr.easy.grid.api.Direction;
import de.amr.easy.grid.api.ObservableDataGrid2D;

/**
 * Maze generator based on Prim's minimum spanning tree algorithm with random edge weights.
 * 
 * @author Armin Reichert
 */
public class PrimMST implements Consumer<Integer> {

	private final ObservableDataGrid2D<Integer, DefaultEdge<Integer>, TraversalState> grid;
	private final PriorityQueue<DefaultWeightedEdge<Integer>> cut = new PriorityQueue<>();
	private final Random rnd = new Random();

	public PrimMST(ObservableDataGrid2D<Integer, DefaultEdge<Integer>, TraversalState> grid) {
		this.grid = grid;
	}

	@Override
	public void accept(Integer start) {
		addToMaze(start);
		while (!cut.isEmpty()) {
			DefaultWeightedEdge<Integer> edge = cut.poll();
			Integer p = edge.either(), q = edge.other(p);
			if (outsideMaze(p) || outsideMaze(q)) {
				grid.addEdge(edge);
				addToMaze(outsideMaze(p) ? p : q);
			}
		}
	}

	private void addToMaze(Integer cell) {
		grid.set(cell, COMPLETED);
		/*@formatter:off*/
		grid.neighbors(cell, Direction.randomOrder())
			.filter(neighbor -> outsideMaze(neighbor))
			.forEach(frontierCell -> {
				grid.set(frontierCell, VISITED);
				cut.add(new DefaultWeightedEdge<>(cell, frontierCell, rnd.nextDouble()));
			});
		/*@formatter:on*/
	}

	private boolean outsideMaze(Integer cell) {
		return grid.get(cell) != COMPLETED;
	}
}