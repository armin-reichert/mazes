package de.amr.easy.maze.alg.mst;

import static de.amr.easy.graph.api.TraversalState.COMPLETED;
import static de.amr.easy.util.GridUtils.permute;

import java.util.Optional;
import java.util.stream.Stream;

import de.amr.easy.data.Partition;
import de.amr.easy.data.PartitionComp;
import de.amr.easy.graph.api.Edge;
import de.amr.easy.graph.api.SimpleEdge;
import de.amr.easy.graph.api.TraversalState;
import de.amr.easy.grid.api.Grid2D;
import de.amr.easy.maze.alg.MazeAlgorithm;

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
	public void run(int start) {
		forest = new Partition<>(grid.vertexStream().boxed());
		while (forest.size() > 1) {
			permute(forest.components()).map(this::findCombiningEdge).filter(Optional::isPresent).map(Optional::get)
					.forEach(this::addEdgeToMaze);
		}
	}

	private void addEdgeToMaze(Edge edge) {
		int u = edge.either(), v = edge.other(u);
		if (!forest.sameComponent(u, v)) {
			grid.addEdge(u, v);
			grid.set(u, COMPLETED);
			grid.set(v, COMPLETED);
			forest.union(u, v);
		}
	}

	private Optional<Edge> findCombiningEdge(PartitionComp<Integer> tree) {
		return permute(tree.elements()).flatMap(this::combiningEdges).findFirst();
	}

	private Stream<Edge> combiningEdges(Integer node) {
		return grid.neighborsPermuted(node).boxed().filter(neighbor -> !forest.sameComponent(node, neighbor))
				.map(neighbor -> new SimpleEdge(node, neighbor));
	}
}