package de.amr.easy.maze.alg.ust;

import java.util.stream.IntStream;

import de.amr.easy.graph.api.TraversalState;
import de.amr.easy.grid.api.Grid2D;
import de.amr.easy.grid.iterators.traversals.CollapsingWalls;

/**
 * Wilson's algorithm where the vertices are selected alternating left-to-right and right-to-left
 * column-wise.
 * 
 * @author Armin Reichert
 */
public class WilsonUSTCollapsingWalls extends WilsonUST {

	public WilsonUSTCollapsingWalls(Grid2D<TraversalState, Integer> grid) {
		super(grid);
	}

	@Override
	protected IntStream cellStream() {
		return new CollapsingWalls(grid).stream();
	}
}