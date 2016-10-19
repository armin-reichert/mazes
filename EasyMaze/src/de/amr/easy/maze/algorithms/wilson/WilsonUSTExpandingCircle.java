package de.amr.easy.maze.algorithms.wilson;

import static de.amr.easy.grid.api.GridPosition.CENTER;

import java.util.stream.Stream;

import de.amr.easy.graph.api.TraversalState;
import de.amr.easy.grid.api.DataGrid2D;
import de.amr.easy.grid.iterators.traversals.ExpandingCircle;

/**
 * Wilson's algorithm where the vertices are selected from an expanding circle.
 * 
 * @author Armin Reichert
 */
public class WilsonUSTExpandingCircle extends WilsonUST {

	public WilsonUSTExpandingCircle(DataGrid2D<TraversalState> grid) {
		super(grid);
	}

	@Override
	protected Stream<Integer> cellStream() {
		return new ExpandingCircle(grid, grid.cell(CENTER), 1, Math.max(grid.numCols(), grid.numRows())).stream();
	}

	@Override
	protected Integer customStartCell(Integer start) {
		return grid.cell(CENTER);
	}
}