package de.amr.easy.maze.algorithms;

import static de.amr.easy.graph.api.TraversalState.COMPLETED;
import static de.amr.easy.graph.api.TraversalState.VISITED;

import java.util.PriorityQueue;
import java.util.Random;
import java.util.function.Consumer;

import de.amr.easy.graph.api.TraversalState;
import de.amr.easy.graph.impl.DefaultEdge;
import de.amr.easy.grid.api.Direction;
import de.amr.easy.grid.api.ObservableDataGrid2D;

/**
 * Maze generator based on Prim's minimum spanning tree algorithm with random edge weights.
 * 
 * @author Armin Reichert
 */
public class PrimMST implements Consumer<Integer> {

	private final ObservableDataGrid2D<Integer, DefaultEdge<Integer>, TraversalState> grid;
	private final PriorityQueue<DefaultEdge<Integer>> cut = new PriorityQueue<>();
	private final Random rnd = new Random();

	public PrimMST(ObservableDataGrid2D<Integer, DefaultEdge<Integer>, TraversalState> grid) {
		this.grid = grid;
	}

	@Override
	public void accept(Integer start) {
		extendMaze(start);
		while (!cut.isEmpty()) {
			DefaultEdge<Integer> edge = cut.poll();
			Integer eitherCell = edge.either(), otherCell = edge.other(eitherCell);
			if (outsideMaze(eitherCell) || outsideMaze(otherCell)) {
				grid.addEdge(eitherCell, otherCell);
				extendMaze(outsideMaze(eitherCell) ? eitherCell : otherCell);
			}
		}
	}

	/**
	 * Adds the given cell to the maze and extends the cut to the rest of the grid with randomly
	 * weighted edges.
	 */
	private void extendMaze(Integer cell) {
		grid.set(cell, COMPLETED);
		/*@formatter:off*/
		grid.neighbors(cell, Direction.valuesPermuted())
			.filter(this::outsideMaze)
			.forEach(frontierCell -> {
				grid.set(frontierCell, VISITED);
				cut.add(new DefaultEdge<>(cell, frontierCell, rnd.nextDouble()));
			});
		/*@formatter:on*/
	}

	private boolean outsideMaze(Integer cell) {
		return grid.get(cell) != COMPLETED;
	}
}