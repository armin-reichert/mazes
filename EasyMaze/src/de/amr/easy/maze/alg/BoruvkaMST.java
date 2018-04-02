package de.amr.easy.maze.alg;

import static de.amr.easy.graph.api.TraversalState.COMPLETED;
import static de.amr.easy.maze.misc.MazeUtils.streamPermuted;

import java.util.stream.Stream;

import de.amr.easy.data.Partition;
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

	public BoruvkaMST(Grid2D<TraversalState, Integer> grid) {
		super(grid);
	}

	private Stream<Integer> componentCellsPermuted(Partition<Integer> partition,
			Partition.EquivClass<Integer> component) {
		return streamPermuted(grid.vertexStream()).filter(cell -> partition.find(cell) == component);
		// return streamPermuted(component.elements());
	}

	@Override
	public void run(Integer start) {
		// initialize forest with single tree for each cell:
		Partition<Integer> forest = new Partition<>(grid.vertexStream()::iterator);
		// while there are different components, merge using the cheapest (here: any) edge
		while (forest.size() > 1) {
			streamPermuted(forest.components()).forEach(component -> {
				componentCellsPermuted(forest, component).forEach(cell -> {
					grid.neighborsPermuted(cell).filter(neighbor -> forest.find(neighbor) != component).findFirst()
							.ifPresent(neighbor -> {
								// add edge {cell, neighbor} to spanning tree
								grid.addEdge(cell, neighbor);
								grid.set(cell, COMPLETED);
								grid.set(neighbor, COMPLETED);
								// merge the components
								forest.union(cell, neighbor);
							});
				});
			});
		}
	}
}