package de.amr.easy.maze.alg.ust;

import static de.amr.easy.graph.api.traversal.TraversalState.COMPLETED;
import static de.amr.easy.graph.api.traversal.TraversalState.VISITED;
import static de.amr.easy.util.StreamUtils.randomElement;

import de.amr.easy.graph.api.traversal.TraversalState;
import de.amr.easy.grid.api.GridGraph2D;
import de.amr.easy.maze.alg.core.MazeAlgorithm;

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
public class AldousBroderUST extends MazeAlgorithm<Void> {

	private int numVisitedCells;
	private int currentCell;

	public AldousBroderUST(GridGraph2D<TraversalState, Void> grid) {
		super(grid);
	}

	@Override
	public void run(int start) {
		currentCell = start;
		grid.set(currentCell, COMPLETED);
		numVisitedCells = 1;
		while (numVisitedCells < grid.numVertices()) {
			visitRandomNeighbor();
		}
	}

	/**
	 * Visits a random neighbor of the current cell and adds it to the maze if visited for the first
	 * time.
	 */
	private void visitRandomNeighbor() {
		int neighbor = randomElement(grid.neighbors(currentCell)).getAsInt();
		if (isCellUnvisited.test(neighbor)) {
			grid.addEdge(currentCell, neighbor);
			grid.set(neighbor, COMPLETED);
			++numVisitedCells;
		}
		currentCell = neighbor;
		// for animation only:
		TraversalState state = grid.get(currentCell);
		grid.set(currentCell, VISITED);
		grid.set(currentCell, state);
	}
}