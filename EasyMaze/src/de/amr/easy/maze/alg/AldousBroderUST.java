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

	private int visitCount;
	private int v;

	public AldousBroderUST(Grid2D<TraversalState, Integer> grid) {
		super(grid);
	}

	@Override
	public void run(int start) {
		v = start;
		grid.set(v, COMPLETED);
		visitCount = 1;
		while (visitCount < grid.numCells()) {
			visitNeighbor();
			showVisit();
		}
	}

	/**
	 * Visits a random neighbor of the current cell and adds it to the maze if visited for the first
	 * time.
	 */
	private void visitNeighbor() {
		int u = grid.randomNeighbor(v).getAsInt();
		if (isCellUnvisited(u)) {
			grid.set(u, COMPLETED);
			++visitCount;
			grid.addEdge(u, v);
		}
		v = u;
	}

	/**
	 * Animates the visit of the current cell by temporarily changing its state to "visited".
	 */
	private void showVisit() {
		TraversalState state = grid.get(v);
		grid.set(v, VISITED);
		grid.set(v, state);
	}
}