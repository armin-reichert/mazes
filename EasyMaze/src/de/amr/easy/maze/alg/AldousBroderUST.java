package de.amr.easy.maze.alg;

import static de.amr.easy.graph.api.TraversalState.COMPLETED;
import static de.amr.easy.graph.api.TraversalState.VISITED;

import de.amr.easy.graph.api.TraversalState;
import de.amr.easy.grid.api.Grid2D;

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
 * @author Armin Reichert
 * 
 * @see<a href= "http://weblog.jamisbuck.org/2011/1/17/maze-generation-aldous-broder-algorithm">Maze
 *        Generation: Aldous-Broder algorithm</a>
 * 
 */
public class AldousBroderUST extends MazeAlgorithm {

	private int mazeCellCount;

	public AldousBroderUST(Grid2D<TraversalState> grid) {
		super(grid);
	}

	@Override
	public void accept(Integer v) {
		grid.set(v, COMPLETED);
		mazeCellCount = 1;
		while (mazeCellCount < grid.numCells()) {
			v = visitRandomNeighbor(v);
		}
	}

	/**
	 * Visits a random neighbor cell and adds it to the maze if it is visited for the first time.
	 * 
	 * @param v
	 *          a maze cell
	 * @return the visited neighbor
	 */
	private Integer visitRandomNeighbor(Integer v) {
		/*@formatter:off*/
		return grid.randomNeighbor(v)
			.map(u -> {
				// if first time visit, add neighbor to maze
				if (isCellUnvisited(u)) {
					grid.set(u, COMPLETED);
					++mazeCellCount;
					grid.addEdge(u, v);
				}
				return u;
			})
			.map(this::animateCellVisit)
			.get();
		/*@formatter:on*/
	}

	/**
	 * Set cell state temporarily to "visited" such that renderer is informed.
	 * 
	 * @param cell
	 *          a grid cell
	 * @return the same cell
	 */
	private Integer animateCellVisit(Integer cell) {
		TraversalState state = grid.get(cell);
		grid.set(cell, VISITED);
		grid.set(cell, state);
		return cell;
	}
}