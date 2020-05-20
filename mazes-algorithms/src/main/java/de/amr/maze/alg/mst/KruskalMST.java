package de.amr.maze.alg.mst;

import static de.amr.datastruct.StreamUtils.permute;
import static de.amr.graph.core.api.TraversalState.COMPLETED;
import static de.amr.graph.core.api.TraversalState.UNVISITED;
import static de.amr.graph.grid.impl.GridFactory.fullGrid;

import de.amr.datastruct.Partition;
import de.amr.graph.core.api.TraversalState;
import de.amr.graph.grid.api.GridGraph2D;
import de.amr.maze.alg.core.MazeGenerator;

/**
 * Maze generator derived from Kruskal's minimum spanning-tree algorithm.
 * 
 * @author Armin Reichert
 * 
 * @see <a href=
 *      "http://weblog.jamisbuck.org/2011/1/3/maze-generation-kruskal-s-algorithm.html">Maze
 *      Generation: Kruskal's Algorithm</a>
 */
public class KruskalMST extends MazeGenerator {

	public KruskalMST(GridGraph2D<TraversalState, Integer> grid) {
		super(grid);
	}

	@Override
	public void createMaze(int x, int y) {
		Partition<Integer> forest = new Partition<>();
		//@formatter:off
		permute( fullGrid(grid.numCols(), grid.numRows(), grid.getTopology(), UNVISITED, 0).edges() )
			.filter(edge -> forest.union(edge.either(), edge.other()))
			.forEach(edge -> {
				int u = edge.either(), v = edge.other();
				grid.addEdge(u, v);
				grid.set(u, COMPLETED);
				grid.set(v, COMPLETED);
			});
		//@formatter:on
	}
}