package de.amr.easy.maze.alg.ust;

import static de.amr.easy.graph.api.traversal.TraversalState.COMPLETED;
import static de.amr.easy.graph.api.traversal.TraversalState.UNVISITED;
import static de.amr.easy.graph.api.traversal.TraversalState.VISITED;
import static de.amr.easy.util.StreamUtils.randomElement;

import de.amr.easy.graph.api.traversal.TraversalState;
import de.amr.easy.maze.alg.core.OrthogonalMazeGenerator;
import de.amr.easy.maze.alg.core.OrthogonalGrid;

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
public class AldousBroderUST extends OrthogonalMazeGenerator {

	private int numVisitedCells;
	private int currentCell;

	public AldousBroderUST(int numCols, int numRows) {
		super(numCols, numRows, false, UNVISITED);
	}

	public AldousBroderUST(OrthogonalGrid grid) {
		super(grid);
	}

	@Override
	public OrthogonalGrid createMaze(int x, int y) {
		run(maze.cell(x, y), maze.numVertices());
		return maze;
	}

	public void run(int start, int limit) {
		currentCell = start;
		maze.set(currentCell, COMPLETED);
		numVisitedCells = 1;
		while (numVisitedCells < limit) {
			visitRandomNeighbor();
		}
	}

	/**
	 * Visits a random neighbor of the current cell and adds it to the maze if visited for the first
	 * time.
	 */
	private void visitRandomNeighbor() {
		int neighbor = randomElement(maze.neighbors(currentCell)).getAsInt();
		if (maze.isUnvisited(neighbor)) {
			maze.addEdge(currentCell, neighbor);
			maze.set(neighbor, COMPLETED);
			++numVisitedCells;
		}
		currentCell = neighbor;
		// for animation only:
		TraversalState state = maze.get(currentCell);
		maze.set(currentCell, VISITED);
		maze.set(currentCell, state);
	}
}