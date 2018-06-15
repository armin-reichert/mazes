package de.amr.easy.maze.alg.ust;

import static de.amr.easy.graph.api.TraversalState.COMPLETED;
import static de.amr.easy.graph.api.TraversalState.VISITED;
import static de.amr.easy.util.StreamUtils.permute;
import static de.amr.easy.util.StreamUtils.randomElement;

import de.amr.easy.graph.api.TraversalState;
import de.amr.easy.grid.api.Grid2D;
import de.amr.easy.maze.alg.MazeAlgorithm;

/**
 * A hybrid algorithm that first uses Aldous/Broder until some fraction of cells are visited and
 * then switches to the Wilson algorithm. Also named "Houston" algorithm.
 * 
 * @see https://news.ycombinator.com/item?id=2123695
 * 
 * @author Armin Reichert
 */
public class AldousBroderWilsonUST extends MazeAlgorithm {

	private int numVisitedCells;
	private int currentCell;

	public AldousBroderWilsonUST(Grid2D<TraversalState, Integer> grid) {
		super(grid);
	}

	@Override
	public void run(int start) {
		int threshold = grid.numCells() * 30 / 100;

		// start with Aldous/Broder
		currentCell = start;
		grid.set(currentCell, COMPLETED);
		numVisitedCells = 1;
		while (numVisitedCells < threshold) {
			visitRandomNeighbor();
		}
		// continue with Wilson
		WilsonUSTRandomCell wilson = new WilsonUSTRandomCell(grid);
		permute(grid.vertices().filter(isCellUnvisited)).forEach(wilson::loopErasedRandomWalk);

	}

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