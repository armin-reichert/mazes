package de.amr.maze.alg.ust;

import static de.amr.datastruct.StreamUtils.permute;
import static de.amr.graph.core.api.TraversalState.UNVISITED;

import de.amr.graph.core.api.TraversalState;
import de.amr.graph.grid.api.GridGraph2D;
import de.amr.maze.alg.core.MazeGenerator;
import de.amr.maze.alg.core.MazeGridFactory;

/**
 * A hybrid algorithm ("Houston") that first uses Aldous/Broder until some fraction of cells is
 * visited and then switches to the Wilson algorithm.
 * <p>
 * I cannot confirm that this algorithm runs faster than Wilson's.
 * 
 * @see https://news.ycombinator.com/item?id=2123695
 * 
 * @author Armin Reichert
 */
public class AldousBroderWilsonUST implements MazeGenerator {

	private GridGraph2D<TraversalState, Integer> grid;

	public AldousBroderWilsonUST(MazeGridFactory factory, int numCols, int numRows) {
		grid = factory.emptyGrid(numCols, numRows, UNVISITED);
	}

	@Override
	public GridGraph2D<TraversalState, Integer> getGrid() {
		return grid;
	}

	@Override
	public GridGraph2D<TraversalState, Integer> createMaze(int x, int y) {
		new AldousBroderUST(grid).run(grid.cell(x, y), Math.round(grid.numVertices() / 3.0f));
		WilsonUSTRandomCell wilson = new WilsonUSTRandomCell(grid);
		permute(grid.vertices().filter(this::isCellUnvisited)).forEach(wilson::loopErasedRandomWalk);
		return grid;
	}
}