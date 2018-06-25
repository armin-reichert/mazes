package de.amr.easy.maze.alg.mst;

import static de.amr.easy.graph.api.traversal.TraversalState.COMPLETED;
import static de.amr.easy.graph.api.traversal.TraversalState.UNVISITED;

import java.util.PriorityQueue;
import java.util.Random;

import de.amr.easy.graph.api.WeightedEdge;
import de.amr.easy.maze.alg.core.ObservableMazeGenerator;
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
public class PrimMST extends ObservableMazeGenerator {

	private PriorityQueue<WeightedEdge<Integer>> cut;
	private Random rnd = new Random();

	public PrimMST(int numCols, int numRows) {
		super(numCols, numRows, false, UNVISITED);
	}

	@Override
	public OrthogonalGrid createMaze(int x, int y) {
		cut = new PriorityQueue<>();
		extendMazeAt(maze.cell(x, y));
		while (!cut.isEmpty()) {
			WeightedEdge<Integer> minEdge = cut.poll();
			int u = minEdge.either(), v = minEdge.other();
			if (maze.isUnvisited(u) || maze.isUnvisited(v)) {
				maze.addEdge(u, v);
				extendMazeAt(maze.isUnvisited(u) ? u : v);
			}
		}
		return maze;
	}

	private void extendMazeAt(int cell) {
		maze.neighbors(cell).filter(maze::isUnvisited).forEach(neighbor -> {
			cut.add(new WeightedEdge<>(cell, neighbor, rnd.nextInt()));
		});
		maze.set(cell, COMPLETED);
	}
}