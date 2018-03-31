package de.amr.easy.maze.alg;

import static de.amr.easy.graph.api.TraversalState.COMPLETED;
import static java.util.stream.Collectors.toList;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import de.amr.easy.data.Partition;
import de.amr.easy.graph.api.TraversalState;
import de.amr.easy.grid.api.Grid2D;

/**
 * Maze generator derived from Boruvka's minimum spanning tree algorithm.
 * 
 * @author Armin Reichert
 * 
 * @see <a href="https://en.wikipedia.org/wiki/Bor%C5%AFvka%27s_algorithm">Boruvka's Algorithm</a>
 */
public class BoruvkaMST extends MazeAlgorithm {

	private Partition<Integer> forest;

	public BoruvkaMST(Grid2D<TraversalState, Integer> grid) {
		super(grid);
	}

	private static <T> Collector<T, ?, List<T>> toShuffledList() {
		return Collectors.collectingAndThen(toList(), list -> {
			Collections.shuffle(list);
			return list;
		});
	}

	private static Stream<Partition.EquivClass> componentsPermuted(Partition<Integer> forest) {
		return forest.components().collect(toShuffledList()).stream();
	}

	private static Stream<Integer> cellsPermuted(Grid2D<TraversalState, Integer> grid) {
		return grid.vertexStream().collect(toShuffledList()).stream();
	}

	@Override
	public void run(Integer start) {
		// initialize forest with single tree for each cell:
		forest = new Partition<>(grid.vertexStream()::iterator);
		// while there are different components, merge using the cheapest (here: any) edge
		while (forest.size() > 1) {
			componentsPermuted(forest).forEach(component -> {
				// TODO: we need a method returning a stream of exactly the elements of a given component
				cellsPermuted(grid).filter(cell -> forest.find(cell) == component).forEach(cell -> {
					grid.neighborsPermuted(cell).filter(neighbor -> forest.find(neighbor) != component).findFirst()
							.ifPresent(neighbor -> {
								// add edge {cell, neighbor} to spanning tree
								grid.addEdge(cell, neighbor);
								grid.set(cell, COMPLETED);
								grid.set(neighbor, COMPLETED);
								// merge the components
								forest.union(forest.find(cell), forest.find(neighbor));
							});
				});
			});
		}
	}
}