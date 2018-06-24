package de.amr.easy.maze.alg.ust;

import static de.amr.easy.graph.api.traversal.TraversalState.COMPLETED;
import static de.amr.easy.graph.api.traversal.TraversalState.UNVISITED;
import static de.amr.easy.graph.api.traversal.TraversalState.VISITED;
import static de.amr.easy.util.StreamUtils.permute;
import static de.amr.easy.util.StreamUtils.randomElement;

import de.amr.easy.graph.api.traversal.TraversalState;
import de.amr.easy.maze.alg.core.ObservableMazeGenerator;
import de.amr.easy.maze.alg.core.OrthogonalGrid;

/**
 * A hybrid algorithm that first uses Aldous/Broder until some fraction of cells are visited and
 * then switches to the Wilson algorithm. Also named "Houston" algorithm.
 * 
 * @see https://news.ycombinator.com/item?id=2123695
 * 
 * @author Armin Reichert
 */
public class AldousBroderWilsonUST extends ObservableMazeGenerator {

	private int numVisitedCells;
	private int currentCell;

	public AldousBroderWilsonUST(int numCols, int numRows) {
		super(numCols, numRows, false, UNVISITED);
	}

	@Override
	public OrthogonalGrid createMaze(int x, int y) {
		int threshold = maze.numVertices() * 30 / 100;
		// start with Aldous/Broder
		currentCell = maze.cell(x, y);
		maze.set(currentCell, COMPLETED);
		numVisitedCells = 1;
		while (numVisitedCells < threshold) {
			visitRandomNeighbor();
		}
		// continue with Wilson
		WilsonUSTRandomCell wilson = new WilsonUSTRandomCell(maze.numCols(), maze.numRows());
		permute(maze.vertices().filter(maze::isUnvisited))
				.forEach(walkStart -> wilson.loopErasedRandomWalk(maze, walkStart));
		return maze;
	}

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