package de.amr.easy.maze.alg;

import static de.amr.easy.graph.api.TraversalState.COMPLETED;

import de.amr.easy.data.Partition;
import de.amr.easy.data.Partition.EquivClass;
import de.amr.easy.graph.api.TraversalState;
import de.amr.easy.grid.api.Dir4;
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

	private final Partition<Integer> forest = new Partition<>();

	public KruskalMST(Grid2D<Dir4,TraversalState, Integer> grid) {
		super(grid);
	}

	@Override
	public void accept(Integer start) {
		grid.fullGridEdgesPermuted().forEach(edge -> {
			Integer either = edge.either(), other = edge.other(either);
			EquivClass eitherTree = forest.find(either), otherTree = forest.find(other);
			if (eitherTree != otherTree) {
				grid.set(either, COMPLETED);
				grid.set(other, COMPLETED);
				grid.addEdge(either, other);
				forest.union(eitherTree, otherTree);
			}
		});
	}
}