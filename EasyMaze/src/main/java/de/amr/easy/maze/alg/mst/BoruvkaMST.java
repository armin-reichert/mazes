package de.amr.easy.maze.alg.mst;

import static de.amr.easy.graph.grid.impl.OrthogonalGrid.emptyGrid;
import static de.amr.easy.graph.pathfinder.api.TraversalState.COMPLETED;
import static de.amr.easy.graph.pathfinder.api.TraversalState.UNVISITED;
import static de.amr.easy.util.StreamUtils.permute;

import java.util.Optional;
import java.util.stream.Stream;

import de.amr.easy.datastruct.Partition;
import de.amr.easy.graph.core.api.Edge;
import de.amr.easy.graph.core.api.UndirectedEdge;
import de.amr.easy.graph.grid.impl.OrthogonalGrid;
import de.amr.easy.maze.alg.core.MazeGenerator;

/**
 * Maze generator derived from Boruvka's minimum spanning tree algorithm.
 * 
 * @author Armin Reichert
 * 
 * @see <a href="http://iss.ices.utexas.edu/?p=projects/galois/benchmarks/mst">Boruvka's
 *      Algorithm</a>
 */
public class BoruvkaMST implements MazeGenerator<OrthogonalGrid> {

	private OrthogonalGrid grid;
	private Partition<Integer> forest;

	public BoruvkaMST(int numCols, int numRows) {
		grid = emptyGrid(numCols, numRows, UNVISITED);
	}

	@Override
	public OrthogonalGrid getGrid() {
		return grid;
	}

	@Override
	public OrthogonalGrid createMaze(int x, int y) {
		forest = new Partition<>();
		grid.vertices().forEach(forest::makeSet);
		while (forest.size() > 1) {
			permute(forest.sets()).map(this::findCombiningEdge).filter(Optional::isPresent)
					.map(Optional::get).forEach(this::combineTrees);
		}
		return grid;
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
		return permute(
				grid.neighbors(cell).filter(neighbor -> forest.find(cell) != forest.find(neighbor))
						.mapToObj(neighbor -> new UndirectedEdge(cell, neighbor)));
	}
}