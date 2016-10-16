package de.amr.easy.maze.algorithms;

import static de.amr.easy.graph.api.TraversalState.COMPLETED;
import static de.amr.easy.graph.api.TraversalState.VISITED;

import java.util.BitSet;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Predicate;

import de.amr.easy.graph.api.TraversalState;
import de.amr.easy.grid.api.ObservableDataGrid2D;

/**
 * Maze generator using a randomized breadth-first-traversal.
 * 
 * @author Armin Reichert
 */
public class RandomBFS extends MazeAlgorithm {

	private final BitSet maze;
	private final List<Integer> frontier = new LinkedList<>();

	public RandomBFS(ObservableDataGrid2D<TraversalState> grid) {
		super(grid);
		maze = new BitSet(grid.numCells());
	}

	@Override
	public void accept(Integer start) {
		Predicate<Integer> outsideMaze = cell -> !maze.get(cell);
		addToMaze(start);
		while (!frontier.isEmpty()) {
			Integer cell = frontier.remove(rnd.nextInt(frontier.size()));
			/*@formatter:off*/
			grid.neighbors(cell)
				.filter(outsideMaze)
				.forEach(neighbor -> {
					addToMaze(neighbor);
					grid.addEdge(cell, neighbor);
				});
			/*@formatter:on*/
			grid.set(cell, COMPLETED);
		}
	}

	private void addToMaze(Integer cell) {
		grid.set(cell, VISITED);
		maze.set(cell);
		frontier.add(cell);
	}
}