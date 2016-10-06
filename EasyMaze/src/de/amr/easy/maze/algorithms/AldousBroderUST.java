package de.amr.easy.maze.algorithms;

import static de.amr.easy.graph.api.TraversalState.COMPLETED;
import static de.amr.easy.graph.api.TraversalState.UNVISITED;
import static de.amr.easy.graph.api.TraversalState.VISITED;

import java.util.Optional;
import java.util.function.Consumer;

import de.amr.easy.graph.api.TraversalState;
import de.amr.easy.graph.impl.DefaultEdge;
import de.amr.easy.grid.api.ObservableDataGrid2D;

/**
 * Let G = (V,E) be a graph with vertices V and edge set E.
 * <p>
 * Aldous-Broder algorithm:
 * <p>
 * Input: G = (V,E)<br>
 * Output: T = (V, W), where W is a subset of E such that T is a spanning tree of G.
 * <p>
 * Let W be the empty set. Add edges to W in the following manner: starting at any vertex v in V,
 * <ol>
 * <li>If all vertices in V have been visited, halt and return T
 * <li>Choose a vertex u uniformly at random from the set of neighbors of v.
 * <li>If u has never been visited before, add the edge (u,v) to the spanning tree.
 * <li>Set v, the current vertex, to be u and return to step 1.
 * </ol>
 * 
 * References:
 * <ul>
 * <li><a href=
 * "https://www.physicsforums.com/threads/matlab-code-for-aldous-broder-algorithm-from-sp a n n i n
 * g -trees-of-a-graph.660566/ ">https://www.physicsforums.com/threads
 * /matlab-code-for-aldous-broder-algorithm-from-sp a n n i n g -trees-of-a-graph.660566/</a> <br>
 * <li><a href= "http://weblog.jamisbuck.org/2011/1/17/maze-generation-aldous-broder-algorithm" >
 * http:// weblog.jamisbuck.org/2011/1/17/maze-generation-aldous-broder-algorithm</a>
 * </ul>
 * 
 * @author Armin Reichert
 */
public class AldousBroderUST implements Consumer<Integer> {

	private final ObservableDataGrid2D<Integer, DefaultEdge<Integer>, TraversalState> grid;
	private int mazeCellCount;

	public AldousBroderUST(ObservableDataGrid2D<Integer, DefaultEdge<Integer>, TraversalState> grid) {
		this.grid = grid;
	}

	@Override
	public void accept(Integer v) {
		grid.set(v, COMPLETED);
		mazeCellCount = 1;
		while (mazeCellCount < grid.numCells()) {
			v = neighbor(v).get();
		}
	}

	private Optional<Integer> neighbor(Integer v) {
		return grid.randomNeighbor(v).map(u -> {
			// animate visit
			TraversalState state = grid.get(u);
			grid.set(u, VISITED);
			grid.set(u, state);
			// if unvisited, add neighbor to maze
			if (grid.get(u) == UNVISITED) {
				grid.set(u, COMPLETED);
				++mazeCellCount;
				grid.addEdge(new DefaultEdge<>(u, v));
			}
			return u;
		});
	}
}