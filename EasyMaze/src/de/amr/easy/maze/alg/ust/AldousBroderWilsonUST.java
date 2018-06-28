package de.amr.easy.maze.alg.ust;

import static de.amr.easy.graph.api.traversal.TraversalState.UNVISITED;
import static de.amr.easy.util.StreamUtils.permute;

import de.amr.easy.maze.alg.core.OrthogonalMazeGenerator;
import de.amr.easy.maze.alg.core.OrthogonalGrid;

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
public class AldousBroderWilsonUST implements OrthogonalMazeGenerator {

	private OrthogonalGrid grid;
	
	public AldousBroderWilsonUST(int numCols, int numRows) {
		grid = OrthogonalGrid.emptyGrid(numCols, numRows, UNVISITED);
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