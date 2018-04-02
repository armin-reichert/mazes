package de.amr.easy.maze.alg;

import static de.amr.easy.graph.api.TraversalState.COMPLETED;

import de.amr.easy.data.Partition;
import de.amr.easy.graph.api.TraversalState;
import de.amr.easy.grid.api.Grid2D;

/**
 * Maze generator derived from Kruskal's minimum spanning tree algorithm.
 * 
 * @author Armin Reichert
 * 
 * @see <a href="http://weblog.jamisbuck.org/2011/1/3/maze-generation-kruskal-s-algorithm.html">Maze
 *      Generation: Kruskal's Algorithm</a>
 */
public class KruskalMST extends MazeAlgorithm {

	public KruskalMST(Grid2D<TraversalState, Integer> grid) {
		super(grid);
	}

	@Override
	public void run(Integer start) {
		final Partition<Integer> forest = new Partition<>();
		grid.fullGridEdgesPermuted().forEach(edge -> {
			Integer u = edge.either(), v = edge.other(u);
			if (forest.find(u) != forest.find(v)) {
				grid.set(u, COMPLETED);
				grid.set(v, COMPLETED);
				grid.addEdge(u, v);
				forest.union(u, v);
			}
		});
	}
}