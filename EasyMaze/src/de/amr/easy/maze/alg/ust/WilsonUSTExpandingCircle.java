package de.amr.easy.maze.alg.ust;

import static de.amr.easy.grid.api.GridPosition.CENTER;
import static java.lang.Math.max;

import java.util.stream.IntStream;

import de.amr.easy.graph.api.traversal.TraversalState;
import de.amr.easy.grid.api.GridGraph2D;
import de.amr.easy.grid.iterators.traversals.ExpandingCircle;

/**
 * Wilson's algorithm where the vertices are selected from an expanding circle.
 * 
 * @author Armin Reichert
 */
public class WilsonUSTExpandingCircle extends WilsonUST {

	public WilsonUSTExpandingCircle(GridGraph2D<TraversalState, Void> grid) {
		super(grid);
	}

	@Override
	public void run(int start) {
		super.run(grid.cell(CENTER));
	}

	@Override
	protected IntStream cellStream() {
		return new ExpandingCircle(grid, grid.cell(CENTER), 1, max(grid.numCols(), grid.numRows())).stream();
	}
}