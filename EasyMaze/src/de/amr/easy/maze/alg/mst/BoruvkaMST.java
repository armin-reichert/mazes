package de.amr.easy.maze.alg.mst;

import static de.amr.easy.util.StreamUtils.permute;

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
		if (forest.find(u) != forest.find(v)) {
			addEdge(u, v);
			forest.union(u, v);
		}
	}

	private Optional<Edge> findCombiningEdge(PartitionComp<Integer> tree) {
		return permute(tree.elements()).flatMap(this::combiningEdges).findFirst();
	}

	private Stream<Edge> combiningEdges(Integer node) {
		return permute(grid.neighbors(node)).filter(neighbor -> forest.find(node) != forest.find(neighbor))
				.mapToObj(neighbor -> new SimpleEdge(node, neighbor));
	}
}