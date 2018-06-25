package de.amr.easy.maze.alg.mst;

import static de.amr.easy.graph.api.traversal.TraversalState.COMPLETED;
import static de.amr.easy.graph.api.traversal.TraversalState.UNVISITED;
import static de.amr.easy.maze.alg.core.OrthogonalGrid.fullGrid;
import static de.amr.easy.util.StreamUtils.permute;

import de.amr.easy.data.Partition;
import de.amr.easy.maze.alg.core.ObservableMazeGenerator;
import de.amr.easy.maze.alg.core.OrthogonalGrid;

/**
 * Maze generator derived from Kruskal's minimum spanning tree algorithm.
 * 
 * @author Armin Reichert
 * 
 * @see <a href="https://en.wikipedia.org/wiki/Kruskal%27s_algorithm">Kruskal's Algorithm -
 *      Wikipedia</a>
 * 
 * @see <a href="http://weblog.jamisbuck.org/2011/1/3/maze-generation-kruskal-s-algorithm.html">Maze
 *      Generation: Kruskal's Algorithm</a>
 */
public class KruskalMST extends ObservableMazeGenerator {

	public KruskalMST(int numCols, int numRows) {
		super(numCols, numRows, false, UNVISITED);
	}

	@Override
	public OrthogonalGrid createMaze(int x, int y) {
		Partition<Integer> forest = new Partition<>();
		permute(fullGrid(maze.numCols(), maze.numRows(), UNVISITED).edges()).forEach(edge -> {
			int u = edge.either(), v = edge.other();
			if (forest.find(u) != forest.find(v)) {
				maze.addEdge(u, v);
				maze.set(u, COMPLETED);
				maze.set(v, COMPLETED);
				forest.union(u, v);
			}
		});
		return maze;
	}
}