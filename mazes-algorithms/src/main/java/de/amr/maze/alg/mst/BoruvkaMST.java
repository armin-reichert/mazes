package de.amr.maze.alg.mst;

import static de.amr.datastruct.StreamUtils.permute;
import static de.amr.graph.core.api.TraversalState.COMPLETED;

import java.util.Optional;
import java.util.stream.Stream;

import de.amr.datastruct.Partition;
import de.amr.graph.core.api.Edge;
import de.amr.graph.core.api.TraversalState;
import de.amr.graph.core.api.UndirectedEdge;
import de.amr.graph.grid.api.GridGraph2D;
import de.amr.maze.alg.core.MazeGenerator;

/**
 * Maze generator derived from Boruvka's minimum spanning tree algorithm.
 * 
 * @author Armin Reichert
 * 
 * @see <a href="http://iss.ices.utexas.edu/?p=projects/galois/benchmarks/mst">Boruvka's
 *      Algorithm</a>
 */
public class BoruvkaMST extends MazeGenerator {

	private Partition<Integer> forest;

	public BoruvkaMST(GridGraph2D<TraversalState, Integer> grid) {
		super(grid);
	}

	@Override
	public void createMaze(int x, int y) {
		forest = new Partition<>();
		grid.vertices().forEach(forest::makeSet);
		while (forest.size() > 1) {
			permute(forest.sets()).map(this::findCombiningEdge).filter(Optional::isPresent).map(Optional::get)
					.forEach(this::combineTrees);
		}
	}

	private void combineTrees(Edge edge) {
		int u = edge.either(), v = edge.other();
		if (forest.find(u) != forest.find(v)) {
			grid.addEdge(u, v);
			grid.set(u, COMPLETED);
			grid.set(v, COMPLETED);
			forest.union(u, v);
		}
	}

	private Optional<Edge> findCombiningEdge(Partition<Integer>.Set tree) {
		return permute(tree.elements()).flatMap(this::inventCombiningEdges).findFirst();
	}

	private Stream<Edge> inventCombiningEdges(int cell) {
		// invent edges combining different subtrees
		return permute(grid.neighbors(cell).filter(neighbor -> forest.find(cell) != forest.find(neighbor))
				.mapToObj(neighbor -> new UndirectedEdge(cell, neighbor)));
	}
}