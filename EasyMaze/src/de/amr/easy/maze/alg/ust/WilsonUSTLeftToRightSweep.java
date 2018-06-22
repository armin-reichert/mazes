package de.amr.easy.maze.alg.ust;

import static de.amr.easy.grid.api.GridPosition.TOP_LEFT;

import java.util.stream.IntStream;

import de.amr.easy.grid.iterators.traversals.LeftToRightSweep;
import de.amr.easy.maze.alg.core.OrthogonalGrid;

/**
 * Wilson's algorithm where the vertices are selected column-wise left-to-right.
 * 
 * @author Armin Reichert
 */
public class WilsonUSTLeftToRightSweep extends WilsonUST {

	public WilsonUSTLeftToRightSweep(OrthogonalGrid grid) {
		super(grid);
	}

	@Override
	public void run(int start) {
		super.run(grid.cell(TOP_LEFT));
	}

	@Override
	protected IntStream randomWalkStartCells() {
		return new LeftToRightSweep(grid).stream();
	}
}