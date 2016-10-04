package de.amr.easy.maze.algorithms;

import static de.amr.easy.graph.api.TraversalState.COMPLETED;
import static de.amr.easy.graph.api.TraversalState.VISITED;

import java.util.Objects;
import java.util.PriorityQueue;
import java.util.Random;
import java.util.function.Consumer;
import java.util.stream.Stream;

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
			if (!inMaze(p) || !inMaze(q)) {
				grid.addEdge(edge);
				addToMaze(inMaze(p) ? q : p);
			}
		}
	}

	private void addToMaze(Integer cell) {
		grid.set(cell, COMPLETED);
		/*@formatter:off*/
		Stream.of(Direction.randomOrder())
			.map(dir -> grid.neighbor(cell, dir))
			.filter(Objects::nonNull)
			.filter(neighbor -> !inMaze(neighbor))
			.forEach(frontierCell -> {
				cut.add(new DefaultWeightedEdge<>(cell, frontierCell, rnd.nextDouble()));
				grid.set(frontierCell, VISITED);
			});
		/*@formatter:on*/
	}

	private boolean inMaze(Integer cell) {
		return grid.get(cell) == COMPLETED;
	}
}
