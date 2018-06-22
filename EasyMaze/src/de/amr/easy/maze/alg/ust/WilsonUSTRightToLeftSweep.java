package de.amr.easy.maze.alg.ust;

import static de.amr.easy.grid.api.GridPosition.BOTTOM_RIGHT;

import java.util.stream.IntStream;

import de.amr.easy.graph.api.traversal.TraversalState;
import de.amr.easy.grid.api.GridGraph2D;
import de.amr.easy.grid.iterators.traversals.RightToLeftSweep;

/**
 * Wilson's algorithm where the vertices are selected column-wise left-to-right.
 * 
 * @author Armin Reichert
 */
public class WilsonUSTRightToLeftSweep extends WilsonUST {

	public WilsonUSTRightToLeftSweep(GridGraph2D<TraversalState, Void> grid) {
		super(grid);
	}

	@Override
	public void run(int start) {
		super.run(grid.cell(BOTTOM_RIGHT));
	}

	@Override
	protected IntStream cellStream() {
		return new RightToLeftSweep(grid).stream();
	}
}