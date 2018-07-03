package de.amr.easy.maze.alg.mst;

import static de.amr.easy.graph.api.traversal.TraversalState.COMPLETED;
import static de.amr.easy.graph.api.traversal.TraversalState.UNVISITED;
import static de.amr.easy.maze.alg.core.OrthogonalGrid.emptyGrid;

import java.util.PriorityQueue;
import java.util.Random;

import de.amr.easy.graph.api.Edge;
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
public class PrimMST implements MazeGenerator<OrthogonalGrid> {

	private OrthogonalGrid grid;
	private PriorityQueue<WeightedEdge<Integer>> cut;
	private Random rnd = new Random();

	public PrimMST(int numCols, int numRows) {
		grid = emptyGrid(numCols, numRows, UNVISITED);
	}

	@Override
	public OrthogonalGrid getGrid() {
		return grid;
	}

	@Override
	public OrthogonalGrid createMaze(int x, int y) {
		cut = new PriorityQueue<>();
		expand(grid.cell(x, y));
		while (!cut.isEmpty()) {
			Edge<Integer> minEdge = cut.poll();
			int u = minEdge.either(), v = minEdge.other();
			if (grid.isUnvisited(u) || grid.isUnvisited(v)) {
				grid.addEdge(u, v);
				expand(grid.isUnvisited(u) ? u : v);
			}
		}
		return grid;
	}

	private void expand(int cell) {
		grid.set(cell, COMPLETED);
		grid.neighbors(cell).filter(grid::isUnvisited).forEach(neighbor -> {
			cut.add(new WeightedEdge<>(cell, neighbor, rnd.nextInt()));
		});
	}
}