package de.amr.easy.maze.alg.wilson;

import static de.amr.easy.grid.api.GridPosition.CENTER;

import java.util.stream.Stream;

import de.amr.easy.graph.api.TraversalState;
import de.amr.easy.grid.api.Grid2D;
import de.amr.easy.grid.api.dir.Dir4;
import de.amr.easy.grid.iterators.traversals.Spiral;

/**
 * Wilson's algorithm where the vertices are selected from an expanding spiral.
 * 
 * @author Armin Reichert
 */
public class WilsonUSTExpandingSpiral extends WilsonUST {

	public WilsonUSTExpandingSpiral(Grid2D<Dir4,TraversalState, Integer> grid) {
		super(grid);
	}

	@Override
	protected Stream<Integer> cellStream() {
		return new Spiral(grid, grid.cell(CENTER)).stream();
	}

	@Override
	protected Integer customStartCell(Integer start) {
		return grid.cell(CENTER);
	}
}