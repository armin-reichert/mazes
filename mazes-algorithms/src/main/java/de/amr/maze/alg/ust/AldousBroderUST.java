package de.amr.maze.alg.ust;

import static de.amr.datastruct.StreamUtils.randomElement;
import static de.amr.graph.core.api.TraversalState.COMPLETED;
import static de.amr.graph.core.api.TraversalState.UNVISITED;
import static de.amr.graph.core.api.TraversalState.VISITED;

import de.amr.graph.core.api.TraversalState;
import de.amr.graph.grid.api.GridGraph2D;
import de.amr.maze.alg.core.MazeGridFactory;
import de.amr.maze.alg.core.MazeGenerator;

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
public class AldousBroderUST implements MazeGenerator {

	private GridGraph2D<TraversalState, Integer> grid;
	private int numVisitedCells;
	private int currentCell;

	public AldousBroderUST(MazeGridFactory factory, int numCols, int numRows) {
		grid = factory.emptyGrid(numCols, numRows, UNVISITED);
	}

	@Override
	public GridGraph2D<TraversalState, Integer> getGrid() {
		return grid;
	}

	public AldousBroderUST(GridGraph2D<TraversalState, Integer> grid) {
		this.grid = grid;
	}

	@Override
	public GridGraph2D<TraversalState, Integer> createMaze(int x, int y) {
		run(grid.cell(x, y), grid.numVertices());
		return grid;
	}

	public void run(int start, int limit) {
		currentCell = start;
		grid.set(currentCell, COMPLETED);
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
		int neighbor = randomElement(grid.neighbors(currentCell)).getAsInt();
		if (isUnvisited(neighbor)) {
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