package de.amr.easy.maze.alg.ust;

import static de.amr.easy.grid.api.GridPosition.BOTTOM_RIGHT;

import java.util.stream.IntStream;

import de.amr.easy.graph.api.SimpleEdge;
import de.amr.easy.graph.api.traversal.TraversalState;
import de.amr.easy.grid.api.GridGraph2D;
import de.amr.easy.grid.iterators.traversals.RightToLeftSweep;

/**
 * Wilson's algorithm where the vertices are selected column-wise left-to-right.
 * 
 * @author Armin Reichert
 */
public class WilsonUSTRightToLeftSweep extends WilsonUST {

	public WilsonUSTRightToLeftSweep(GridGraph2D<TraversalState, SimpleEdge> grid) {
		super(grid);
	}

	@Override
	protected IntStream cellStream() {
		return new RightToLeftSweep(grid).stream();
	}

	@Override
	protected int customizedStartCell(int start) {
		return grid.cell(BOTTOM_RIGHT);
	}
}