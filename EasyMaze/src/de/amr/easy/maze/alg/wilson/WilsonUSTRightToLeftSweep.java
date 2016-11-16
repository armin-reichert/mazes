package de.amr.easy.maze.alg.wilson;

import static de.amr.easy.grid.api.GridPosition.BOTTOM_RIGHT;

import java.util.stream.IntStream;

import de.amr.easy.graph.api.TraversalState;
import de.amr.easy.grid.api.Grid2D;
import de.amr.easy.grid.iterators.traversals.RightToLeftSweep;

/**
 * Wilson's algorithm where the vertices are selected column-wise left-to-right.
 * 
 * @author Armin Reichert
 */
public class WilsonUSTRightToLeftSweep extends WilsonUST {

	public WilsonUSTRightToLeftSweep(Grid2D<TraversalState, Integer> grid) {
		super(grid);
	}

	@Override
	protected IntStream cellStream() {
		return new RightToLeftSweep(grid).stream();
	}

	@Override
	protected Integer customStartCell(Integer start) {
		return grid.cell(BOTTOM_RIGHT);
	}
}