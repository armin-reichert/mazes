package de.amr.easy.maze.alg.mst;

import static de.amr.easy.graph.api.traversal.TraversalState.COMPLETED;

import java.util.PriorityQueue;
import java.util.Random;

import de.amr.easy.graph.api.WeightedEdge;
import de.amr.easy.maze.alg.core.MazeGenerator;
import de.amr.easy.maze.alg.core.OrthogonalGrid;

/**
 * Maze generator based on Prim's minimum spanning tree algorithm with random edge weights.
 * 
 * @author Armin Reichert
 * 
 * @see <a href="http://weblog.jamisbuck.org/2011/1/10/maze-generation-prim-s-algorithm.html">Maze
 *      Generation: Prim's Algorithm</a>
 * @see <a href="https://en.wikipedia.org/wiki/Prim%27s_algorithm">Wikipedia: Prim's Algorithm</a>
 */
public class PrimMST implements MazeGenerator {

	private final OrthogonalGrid grid;
	private final PriorityQueue<WeightedEdge<Integer>> cut = new PriorityQueue<>();
	private final Random rnd = new Random();

	public PrimMST(OrthogonalGrid grid) {
		this.grid = grid;
	}

	@Override
	public void run(int start) {
		extendMazeAt(start);
		while (!cut.isEmpty()) {
			WeightedEdge<Integer> edge = cut.poll();
			int either = edge.either(), other = edge.other();
			if (outsideMaze(either) || outsideMaze(other)) {
				grid.addEdge(either, other);
				extendMazeAt(outsideMaze(either) ? either : other);
			}
		}
	}

	private void extendMazeAt(int cell) {
		grid.neighbors(cell).filter(this::outsideMaze).forEach(neighbor -> {
			cut.add(new WeightedEdge<>(cell, neighbor, rnd.nextInt()));
		});
		grid.set(cell, COMPLETED);
	}

	private boolean outsideMaze(int cell) {
		return grid.get(cell) != COMPLETED;
	}
}