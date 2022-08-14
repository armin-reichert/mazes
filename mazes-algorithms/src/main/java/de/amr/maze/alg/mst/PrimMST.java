package de.amr.maze.alg.mst;

import static de.amr.graph.core.api.TraversalState.COMPLETED;

import java.util.PriorityQueue;

import de.amr.graph.core.api.TraversalState;
import de.amr.graph.core.api.WeightedEdge;
import de.amr.graph.grid.api.GridGraph2D;
import de.amr.maze.alg.core.MazeGenerator;

/**
 * Maze generator based on Prim's minimum spanning tree algorithm with random edge weights.
 * 
 * @author Armin Reichert
 * 
 * @see <a href="http://weblog.jamisbuck.org/2011/1/10/maze-generation-prim-s-algorithm.html">Maze Generation: Prim's
 *      Algorithm</a>
 * @see <a href="https://en.wikipedia.org/wiki/Prim%27s_algorithm">Wikipedia: Prim's Algorithm</a>
 */
public class PrimMST extends MazeGenerator {

	private PriorityQueue<WeightedEdge<Integer>> cut;

	public PrimMST(GridGraph2D<TraversalState, Integer> grid) {
		super(grid);
	}

	@Override
	public void createMaze(int x, int y) {
		cut = new PriorityQueue<>();
		expand(grid.cell(x, y));
		while (!cut.isEmpty()) {
			var minWeightEdge = cut.poll();
			int u = minWeightEdge.either();
			int v = minWeightEdge.other();
			if (isCellUnvisited(u) || isCellUnvisited(v)) {
				grid.addEdge(u, v);
				expand(isCellUnvisited(u) ? u : v);
			}
		}
	}

	private void expand(int cell) {
		grid.set(cell, COMPLETED);
		grid.neighbors(cell).filter(this::isCellUnvisited)
				.forEach(neighbor -> cut.add(new WeightedEdge<>(cell, neighbor, rnd.nextInt())));
	}
}