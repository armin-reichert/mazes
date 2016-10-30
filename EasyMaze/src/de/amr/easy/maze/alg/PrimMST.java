package de.amr.easy.maze.alg;

import static de.amr.easy.graph.api.TraversalState.COMPLETED;
import static de.amr.easy.graph.api.TraversalState.VISITED;

import java.util.PriorityQueue;

import de.amr.easy.graph.api.TraversalState;
import de.amr.easy.graph.api.WeightedEdge;
import de.amr.easy.grid.api.Grid2D;

/**
 * Maze generator based on Prim's minimum spanning tree algorithm with random edge weights.
 * 
 * @author Armin Reichert
 * 
 * @see <a href="http://weblog.jamisbuck.org/2011/1/10/maze-generation-prim-s-algorithm.html">Maze
 *      Generation: Prim's Algorithm</a>
 */
public class PrimMST extends MazeAlgorithm {

	private final PriorityQueue<WeightedEdge<Integer, Double>> cut = new PriorityQueue<>();

	public PrimMST(Grid2D<TraversalState> grid) {
		super(grid);
	}

	@Override
	public void accept(Integer start) {
		extendMaze(start);
		while (!cut.isEmpty()) {
			WeightedEdge<Integer, Double> edge = cut.poll();
			Integer either = edge.either(), other = edge.other(either);
			if (outsideMaze(either) || outsideMaze(other)) {
				grid.addEdge(either, other);
				extendMaze(outsideMaze(either) ? either : other);
			}
		}
	}

	private void extendMaze(Integer cell) {
		grid.neighbors(cell).filter(this::outsideMaze).forEach(neighbor -> {
			grid.set(neighbor, VISITED);
			cut.add(new WeightedEdge<>(cell, neighbor, rnd.nextDouble()));
		});
		grid.set(cell, COMPLETED);
	}

	private boolean outsideMaze(Integer cell) {
		return grid.get(cell) != COMPLETED;
	}
}