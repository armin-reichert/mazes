package de.amr.easy.maze.alg;

import static de.amr.easy.graph.api.TraversalState.COMPLETED;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
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

	private IntStream cellsPermuted() {
		List<Integer> cells = grid.vertexStream().collect(Collectors.toList());
		Collections.shuffle(cells);
		return cells.stream().mapToInt(Integer::intValue);
	}

	private Stream<Partition.EquivClass> componentsPermuted(Partition<Integer> forest) {
		List<Partition.EquivClass> components = forest.components().collect(Collectors.toList());
		Collections.shuffle(components);
		return components.stream();
	}

	@Override
	public void run(Integer start) {
		// initialize forest with single tree for each cell:
		forest = new Partition<>();
		grid.vertexStream().forEach(cell -> forest.find(cell));
		while (forest.size() > 1) {
			componentsPermuted(forest).forEach(tree -> {
				// TODO: we need a method returning a stream over just the elements of a component
				cellsPermuted().filter(cell -> forest.find(cell) == tree).forEach(cell -> {
					grid.neighborsPermuted(cell).filter(neighbor -> forest.find(neighbor) != tree).findFirst()
							.ifPresent(neighbor -> {
								// add (cell, neighbor) as an edge from the current tree to another component
								grid.set(cell, COMPLETED);
								grid.set(neighbor, COMPLETED);
								grid.addEdge(cell, neighbor);
								// merge the components
								forest.union(forest.find(cell), forest.find(neighbor));
							});
				});
			});
		}
	}
}