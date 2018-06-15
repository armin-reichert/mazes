package de.amr.easy.maze.alg.mst;

import static de.amr.easy.util.StreamUtils.permute;

import java.util.stream.Stream;

import de.amr.easy.data.Partition;
import de.amr.easy.graph.api.SimpleEdge;
import de.amr.easy.graph.api.TraversalState;
import de.amr.easy.grid.api.Grid2D;
import de.amr.easy.maze.alg.core.MazeAlgorithm;

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
public class KruskalMST extends MazeAlgorithm<SimpleEdge> {

	public KruskalMST(Grid2D<TraversalState, SimpleEdge> grid) {
		super(grid);
	}

	@Override
	public void run(int start) {
		Partition<Integer> forest = new Partition<>();
		grid.fill();
		Stream<SimpleEdge> edges = permute(grid.edges());
		grid.removeEdges();
		edges.forEach(edge -> {
			int u = edge.either(), v = edge.other();
			if (forest.find(u) != forest.find(v)) {
				addTreeEdge(u, v);
				forest.union(u, v);
			}
		});
	}
}