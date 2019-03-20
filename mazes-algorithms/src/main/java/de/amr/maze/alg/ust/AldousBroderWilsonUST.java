package de.amr.maze.alg.ust;

import static de.amr.datastruct.StreamUtils.permute;
import static de.amr.graph.core.api.TraversalState.UNVISITED;
import static de.amr.graph.grid.impl.OrthogonalGrid.emptyGrid;

import de.amr.graph.grid.impl.OrthogonalGrid;
import de.amr.maze.alg.core.MazeGenerator;

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
public class AldousBroderWilsonUST implements MazeGenerator<OrthogonalGrid> {

	private OrthogonalGrid grid;

	public AldousBroderWilsonUST(int numCols, int numRows) {
		grid = emptyGrid(numCols, numRows, UNVISITED);
	}

	@Override
	public OrthogonalGrid getGrid() {
		return grid;
	}

	@Override
	public OrthogonalGrid createMaze(int x, int y) {
		new AldousBroderUST(grid).run(grid.cell(x, y), Math.round(grid.numVertices() / 3.0f));
		WilsonUSTRandomCell wilson = new WilsonUSTRandomCell(grid);
		permute(grid.vertices().filter(grid::isUnvisited)).forEach(wilson::loopErasedRandomWalk);
		return grid;
	}
}