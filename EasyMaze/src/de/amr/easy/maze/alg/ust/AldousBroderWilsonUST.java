package de.amr.easy.maze.alg.ust;

import static de.amr.easy.graph.api.traversal.TraversalState.COMPLETED;
import static de.amr.easy.graph.api.traversal.TraversalState.VISITED;
import static de.amr.easy.util.StreamUtils.permute;
import static de.amr.easy.util.StreamUtils.randomElement;

import de.amr.easy.graph.api.SimpleEdge;
import de.amr.easy.graph.api.traversal.TraversalState;
import de.amr.easy.grid.api.GridGraph2D;
import de.amr.easy.maze.alg.core.MazeAlgorithm;

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

	public AldousBroderWilsonUST(GridGraph2D<TraversalState, SimpleEdge> grid) {
		super(grid);
	}

	@Override
	public void run(int start) {
		int threshold = grid.numVertices() * 30 / 100;

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