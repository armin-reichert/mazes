package de.amr.easy.maze.alg.mst;

import static de.amr.easy.util.StreamUtils.permute;

import de.amr.easy.data.Partition;
import de.amr.easy.graph.api.TraversalState;
import de.amr.easy.grid.api.Grid2D;
import de.amr.easy.maze.alg.MazeAlgorithm;

/**
 * Maze generator derived from Kruskal's minimum spanning tree algorithm.
 * 
 * @author Armin Reichert
 * 
 * @see <a href="http://weblog.jamisbuck.org/2011/1/3/maze-generation-kruskal-s-algorithm.html">Maze
 *      Generation: Kruskal's Algorithm</a>
 */
public class KruskalMST extends MazeAlgorithm {

	private Partition<Integer> forest;

	public KruskalMST(Grid2D<TraversalState, Integer> grid) {
		super(grid);
	}

	@Override
	public void run(int start) {
		forest = new Partition<>(grid.vertexStream().boxed());
		permute(grid.fullGridEdges()).forEach(edge -> {
			int u = edge.either(), v = edge.other(u);
			if (!forest.sameComponent(u, v)) {
				addEdge(u, v);
				forest.union(u, v);
			}
		});
	}
}