package de.amr.easy.maze.alg;

import static de.amr.easy.graph.api.TraversalState.COMPLETED;
import static de.amr.easy.maze.misc.MazeUtils.streamPermuted;

import java.util.Optional;
import java.util.stream.Stream;

import de.amr.easy.data.Partition;
import de.amr.easy.data.PartitionComp;
import de.amr.easy.graph.api.SimpleEdge;
import de.amr.easy.graph.api.TraversalState;
import de.amr.easy.grid.api.Grid2D;

/**
 * Maze generator derived from Boruvka's minimum spanning tree algorithm.
 * 
 * @author Armin Reichert
 * 
 * @see <a href="http://iss.ices.utexas.edu/?p=projects/galois/benchmarks/mst">Boruvka's
 *      Algorithm</a>
 */
public class BoruvkaMST extends MazeAlgorithm {

	private Partition<Integer> forest;

	public BoruvkaMST(Grid2D<TraversalState, Integer> grid) {
		super(grid);
	}

	@Override
	public void run(Integer start) {
		forest = new Partition<>(grid.vertexStream()::iterator);
		while (forest.size() > 1) {
			streamPermuted(forest.components()).map(this::findMinCrossingEdge).filter(Optional::isPresent).map(Optional::get)
					.forEach(edge -> {
						Integer u = edge.either(), v = edge.other(u);
						if (!forest.sameComponent(u, v)) {
							forest.union(u, v);
							grid.addEdge(u, v);
							grid.set(u, COMPLETED);
							grid.set(v, COMPLETED);
						}
					});
		}
	}

	private Optional<SimpleEdge<Integer>> findMinCrossingEdge(PartitionComp<Integer> tree) {
		return streamPermuted(tree.elements()).flatMap(this::crossingEdges).findFirst();
	}

	private Stream<SimpleEdge<Integer>> crossingEdges(Integer node) {
		return grid.neighborsPermuted(node).boxed().filter(neighbor -> forest.sameComponent(node, neighbor))
				.map(neighbor -> new SimpleEdge<>(node, neighbor));
	}
}