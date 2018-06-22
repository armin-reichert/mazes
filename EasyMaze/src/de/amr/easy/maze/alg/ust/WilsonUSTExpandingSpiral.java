package de.amr.easy.maze.alg.ust;

import static de.amr.easy.grid.api.GridPosition.CENTER;

import java.util.stream.IntStream;

import de.amr.easy.graph.api.traversal.TraversalState;
import de.amr.easy.grid.api.GridGraph2D;
import de.amr.easy.grid.iterators.traversals.Spiral;

/**
 * Wilson's algorithm where the vertices are selected from an expanding spiral.
 * 
 * @author Armin Reichert
 */
public class WilsonUSTExpandingSpiral extends WilsonUST {

	public WilsonUSTExpandingSpiral(GridGraph2D<TraversalState, Void> grid) {
		super(grid);
	}

	@Override
	protected IntStream cellStream() {
		return new Spiral(grid, grid.cell(CENTER)).stream();
	}

	@Override
	protected int customizedStartCell(int start) {
		return grid.cell(CENTER);
	}
}