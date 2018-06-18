package de.amr.easy.maze.alg.mst;

import static de.amr.easy.util.StreamUtils.permute;

import java.util.Optional;
import java.util.stream.Stream;

import de.amr.easy.data.Partition;
import de.amr.easy.graph.api.Edge;
import de.amr.easy.graph.api.SimpleEdge;
import de.amr.easy.graph.api.traversal.TraversalState;
import de.amr.easy.grid.api.GridGraph2D;
import de.amr.easy.maze.alg.core.MazeAlgorithm;

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

	public BoruvkaMST(GridGraph2D<TraversalState, SimpleEdge> grid) {
		super(grid);
	}

	@Override
	public void run(int start) {
		forest = new Partition<>();
		grid.vertices().forEach(forest::makeSet);
		while (forest.size() > 1) {
			permute(forest.sets()).map(this::findCombiningEdge).filter(Optional::isPresent).map(Optional::get)
					.forEach(this::addEdge);
		}
	}

	private void addEdge(Edge edge) {
		int u = edge.either(), v = edge.other();
		if (forest.find(u) != forest.find(v)) {
			addTreeEdge(u, v);
			forest.union(u, v);
		}
	}

	private Optional<Edge> findCombiningEdge(Partition<Integer>.Set tree) {
		return permute(tree.elements()).flatMap(this::combiningEdges).findFirst();
	}

	private Stream<Edge> combiningEdges(int cell) {
		return permute(grid.neighbors(cell)).filter(neighbor -> forest.find(cell) != forest.find(neighbor))
				.mapToObj(neighbor -> new SimpleEdge(cell, neighbor));
	}
}