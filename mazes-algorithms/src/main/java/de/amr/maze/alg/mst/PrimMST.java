package de.amr.maze.alg.mst;

import static de.amr.graph.core.api.TraversalState.COMPLETED;
import static de.amr.graph.core.api.TraversalState.UNVISITED;

import java.util.PriorityQueue;
import java.util.Random;

import de.amr.graph.core.api.TraversalState;
import de.amr.graph.core.api.WeightedEdge;
import de.amr.graph.grid.api.GridGraph2D;
import de.amr.maze.alg.core.MazeGenerator;
import de.amr.maze.alg.core.MazeGridFactory;

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

	private GridGraph2D<TraversalState, Integer> grid;
	private PriorityQueue<WeightedEdge<Integer>> cut;
	private Random rnd = new Random();

	public PrimMST(MazeGridFactory factory, int numCols, int numRows) {
		grid = factory.emptyGrid(numCols, numRows, UNVISITED);
	}

	@Override
	public GridGraph2D<TraversalState, Integer> getGrid() {
		return grid;
	}

	@Override
	public GridGraph2D<TraversalState, Integer> createMaze(int x, int y) {
		cut = new PriorityQueue<>();
		expand(grid.cell(x, y));
		while (!cut.isEmpty()) {
			WeightedEdge<Integer> minEdge = cut.poll();
			int u = minEdge.either(), v = minEdge.other();
			if (isCellUnvisited(u) || isCellUnvisited(v)) {
				grid.addEdge(u, v);
				expand(isCellUnvisited(u) ? u : v);
			}
		}
		return grid;
	}

	private void expand(int cell) {
		grid.set(cell, COMPLETED);
		grid.neighbors(cell).filter(this::isCellUnvisited).forEach(neighbor -> {
			cut.add(new WeightedEdge<>(cell, neighbor, rnd.nextInt()));
		});
	}
}