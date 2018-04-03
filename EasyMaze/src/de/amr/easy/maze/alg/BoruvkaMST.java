package de.amr.easy.maze.alg;

import static de.amr.easy.graph.api.TraversalState.COMPLETED;
import static de.amr.easy.maze.misc.MazeUtils.streamPermuted;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import de.amr.easy.data.Partition;
import de.amr.easy.data.Partition.EquivClass;
import de.amr.easy.graph.api.Edge;
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
			Iterable<Partition.EquivClass<Integer>> treesPermuted = streamPermuted(forest.components())::iterator;
			for (Partition.EquivClass<Integer> tree : treesPermuted) {
				findMinOutgoingEdge(tree).ifPresent(minEdge -> {
					Integer u = minEdge.either(), v = minEdge.other(u);
					grid.addEdge(u, v);
					grid.set(u, COMPLETED);
					grid.set(v, COMPLETED);
					forest.union(u, v);
				});
			}
		}
	}

	private Optional<Edge<Integer>> findMinOutgoingEdge(EquivClass<Integer> tree) {
		Iterable<Integer> cells = streamPermuted(tree.elements())::iterator;
		for (Integer cell : cells) {
			Iterable<Integer> neighbors = (Iterable<Integer>) grid.neighborsPermuted(cell)::iterator;
			for (Integer neighbor : neighbors) {
				if (forest.find(cell) != forest.find(neighbor)) {
					return Optional.of(new SimpleEdge<>(cell, neighbor));
				}
			}
		}
		return Optional.empty();
	}
}