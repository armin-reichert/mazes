package de.amr.easy.maze.alg.mst;

import static de.amr.easy.graph.api.traversal.TraversalState.COMPLETED;

import java.util.PriorityQueue;

import de.amr.easy.graph.api.WeightedEdge;
import de.amr.easy.graph.api.traversal.TraversalState;
import de.amr.easy.grid.api.GridGraph2D;
import de.amr.easy.maze.alg.core.MazeAlgorithm;

/**
 * Maze generator based on Prim's minimum spanning tree algorithm with random edge weights.
 * 
 * @author Armin Reichert
 * 
 * @see <a href="http://weblog.jamisbuck.org/2011/1/10/maze-generation-prim-s-algorithm.html">Maze
 *      Generation: Prim's Algorithm</a>
 * @see <a href="https://en.wikipedia.org/wiki/Prim%27s_algorithm">Wikipedia: Prim's Algorithm</a>
 */
public class PrimMST extends MazeAlgorithm<Void> {

	private final PriorityQueue<WeightedEdge<Integer>> cut = new PriorityQueue<>();

	public PrimMST(GridGraph2D<TraversalState, Void> grid) {
		super(grid);
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