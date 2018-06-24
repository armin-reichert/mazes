package de.amr.easy.maze.alg.mst;

import static de.amr.easy.graph.api.traversal.TraversalState.COMPLETED;
import static de.amr.easy.graph.api.traversal.TraversalState.UNVISITED;
import static de.amr.easy.util.StreamUtils.permute;

import java.util.Optional;
import java.util.stream.Stream;

import de.amr.easy.data.Partition;
import de.amr.easy.graph.api.Edge;
import de.amr.easy.graph.api.SimpleEdge;
import de.amr.easy.maze.alg.core.ObservableMazeGenerator;
import de.amr.easy.maze.alg.core.OrthogonalGrid;

/**
 * Maze generator derived from Boruvka's minimum spanning tree algorithm.
 * 
 * @author Armin Reichert
 * 
 * @see <a href="http://iss.ices.utexas.edu/?p=projects/galois/benchmarks/mst">Boruvka's
 *      Algorithm</a>
 */
public class BoruvkaMST extends ObservableMazeGenerator {

	private Partition<Integer> forest;

	public BoruvkaMST(int numCols, int numRows) {
		super(numCols, numRows, false, UNVISITED);
	}

	@Override
	public OrthogonalGrid createMaze(int x, int y) {
		forest = new Partition<>();
		maze.vertices().forEach(forest::makeSet);
		while (forest.size() > 1) {
			permute(forest.sets()).map(this::findCombiningEdge).filter(Optional::isPresent).map(Optional::get)
					.forEach(this::addEdge);
		}
		return maze;
	}

	private void addEdge(Edge<Void> edge) {
		int u = edge.either(), v = edge.other();
		if (forest.find(u) != forest.find(v)) {
			maze.addEdge(u, v);
			maze.set(u, COMPLETED);
			maze.set(v, COMPLETED);
			forest.union(u, v);
		}
	}

	private Optional<Edge<Void>> findCombiningEdge(Partition<Integer>.Set tree) {
		return permute(tree.elements()).flatMap(this::combiningEdges).findFirst();
	}

	private Stream<Edge<Void>> combiningEdges(int cell) {
		return permute(maze.neighbors(cell)).filter(neighbor -> forest.find(cell) != forest.find(neighbor))
				.mapToObj(neighbor -> new SimpleEdge<>(cell, neighbor));
	}
}